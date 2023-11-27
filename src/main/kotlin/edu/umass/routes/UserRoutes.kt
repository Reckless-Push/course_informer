/**
 * This file contains the routes for the User table.
 *
 * @file UserRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.User
import edu.umass.models.UserInfo
import edu.umass.models.UserSession
import edu.umass.plugins.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions

/**
 * Defines the routes for the User table.
 *
 * @receiver The Route on which to define the routes.
 */
fun Route.userRoutes() {
    listUsers()
    getUser()
    addUser()
    updateUser()
    deleteUser()
}

/**
 * Route to list all users.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.listUsers() {
    get("/user") { call.respond(mapOf("user_table" to dao.allUsers())) }
}

/**
 * Route to get a user by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getUser() {
    get("/user/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }

        val user = dao.user(id)
        user?.let { call.respond(user) } ?: call.respond(HttpStatusCode.NotFound, "No user with id $id")
    }
}

/**
 * Route to add a new user.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.addUser() {
    post("/user") {
        try {
            val userSession: UserSession? = call.sessions.get()
            userSession?.let {
                val userInfo = getUserInfoFromSession(userSession)
                val user = createUserFromUserInfo(userInfo)
                val newUser = dao.addNewUser(user)
                newUser?.let { call.respond(newUser) }
                    ?: call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
            }
                ?: run {
                    val redirectUrl =
                        URLBuilder("https://localhost:8443/login").run {
                            parameters.append("redirectUrl", call.request.uri)
                            build()
                        }
                    call.respondRedirect(redirectUrl)
                }
        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
}

/**
 * Route to update a user.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.updateUser() {
    post("/user/{id}") {
        try {
            // Receiving a User object instead of Equipment
            val user: User = call.receive<User>()
            val id = call.parameters["id"]?.toIntOrNull()
            id
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                    return@post
                }
            val updatedUser = dao.editUser(user, id)
            updatedUser.let { call.respond(updatedUser) }
        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
}

/**
 * Route to delete a user by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.deleteUser() {
    get("/user/delete/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }
        val user = dao.deleteUser(id)
        if (user) {
            call.respondText("Deleted user $id", status = HttpStatusCode.Accepted)
        } else {
            call.respond(HttpStatusCode.NotFound, "No user with id $id")
        }
    }
}

/**
 * Gets the user info from the session.
 *
 * @param userSession The user session.
 * @return The user info.
 */
private suspend fun getUserInfoFromSession(userSession: UserSession): UserInfo =
    httpClient
        .get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers { append(HttpHeaders.Authorization, "Bearer ${userSession.token}") }
        }
        .body()

/**
 * Creates a user from the user info.
 *
 * @param userInfo The user info.
 * @return The user.
 */
private fun createUserFromUserInfo(userInfo: UserInfo): User =
    User(
        id = userInfo.id.toInt(),
        firstName = userInfo.givenName,
        lastName = userInfo.familyName,
        email = userInfo.email,
    )
