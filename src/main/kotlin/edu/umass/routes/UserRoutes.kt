/**
 * This file contains the routes for the User table.
 *
 * @file UserRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.Course
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
import java.util.UUID

/**
 * Defines the routes for the User table.
 *
 * @receiver The Route on which to define the routes.
 */
fun Route.userRoutes() {
    listUsers()
    getUser()
    getCurrentUser()
    addUser()
    updateUser()
    deleteUser()
    addFavoriteCourse()
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
    get("/user/{uuid}") {
        val uuid = UUID.fromString(call.parameters["uuid"])
        uuid
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed uuid")
                return@get
            }

        val user = dao.user(uuid)
        user?.let { call.respond(user) }
            ?: call.respond(HttpStatusCode.NotFound, "No user with id $uuid")
    }
}

/**
 * Route to get the current user.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getCurrentUser() {
    get("/user/current") {
        val userSession: UserSession? = call.sessions.get()
        userSession?.let {
            val userInfo = getUserInfoFromSession(userSession)
            val user = dao.user(createUserFromUserInfo(userInfo)?.uuid!!)
            user
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email address")
                    return@get
                }
            user.let { call.respond(user) }
        }
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
                user
                    ?: run {
                        call.respond(HttpStatusCode.BadRequest, "Invalid email address")
                        return@post
                    }
                val newUser = dao.addNewUser(user)
                newUser?.let { call.respond(newUser) }
                    ?: call.respond(HttpStatusCode.BadRequest, "Missing or malformed uuid")
            }
                ?: run {
                    val redirectUrl =
                        URLBuilder("${System.getenv("BASE_URL")}/login").run {
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
    post("/user/{uuid}") {
        try {
            val user: User = call.receive<User>()
            val uuid = UUID.fromString(call.parameters["uuid"])
            uuid
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Missing or malformed uuid")
                    return@post
                }
            val updatedUser = dao.editUser(user, uuid)
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
    get("/user/delete/{uuid}") {
        val uuid = UUID.fromString(call.parameters["uuid"])
        uuid
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }
        val user = dao.deleteUser(uuid)
        if (user) {
            call.respondText("Deleted user $uuid", status = HttpStatusCode.Accepted)
        } else {
            call.respond(HttpStatusCode.NotFound, "No user with id $uuid")
        }
    }
}

/**
 * Route to add a favorite course.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.addFavoriteCourse() {
    post("/user/current/star") {
        val userSession: UserSession? = call.sessions.get()
        userSession?.let {
            val userInfo = getUserInfoFromSession(userSession)
            val user = dao.user(createUserFromUserInfo(userInfo)?.uuid!!)
            user
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email address")
                    return@post
                }
            val course: Course = call.receive<Course>()
            if (user.favoriteCourses.any { it.id == course.id }) {
                call.respond(HttpStatusCode.BadRequest, "Course already in favorites")
            } else {
                val updatedUser = user.copy(favoriteCourses = user.favoriteCourses + course)
                val isUpdated = dao.editUser(updatedUser, updatedUser.uuid!!)
                if (isUpdated) {
                    call.respond(HttpStatusCode.OK, "${updatedUser.favoriteCourses}")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Failed to add favorite course")
                }
            }
        }
    }
}

/**
 * Creates a user from the user info.
 *
 * @param userInfo The user info.
 * @return The user.
 */
internal fun createUserFromUserInfo(userInfo: UserInfo): User? {
    if (userInfo.hd != "umass.edu") {
        return null
    }
    return User(
        uuid = UUID.nameUUIDFromBytes(userInfo.id.toByteArray()),
        firstName = userInfo.givenName,
        lastName = userInfo.familyName,
        email = userInfo.email,
    )
}

/**
 * Gets the user info from the session.
 *
 * @param userSession The user session.
 * @return The user info.
 */
internal suspend fun getUserInfoFromSession(userSession: UserSession): UserInfo =
    httpClient
        .get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers { append(HttpHeaders.Authorization, "Bearer ${userSession.token}") }
        }
        .body()
