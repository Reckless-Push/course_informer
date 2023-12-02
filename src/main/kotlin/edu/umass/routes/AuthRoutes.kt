/**
 * This file contains the routes for authorization.
 *
 * @file AuthRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.UserInfo
import edu.umass.models.UserSession
import edu.umass.plugins.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.request.uri
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

/**
 * Defines the routes for authorization.
 *
 * @receiver The Routing on which to define the routes.
 */
fun Route.authRoutes() {
    authenticate("auth-oauth-google") {
        get("/login") {}

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
            val userSession = UserSession(principal!!.state!!, principal.accessToken)
            call.sessions.set(userSession)
            val response: HttpResponse =
                httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers { append(HttpHeaders.Authorization, "Bearer ${userSession.token}") }
                }
            if (response.status == HttpStatusCode.OK) {
                val userInfo: UserInfo = response.body<UserInfo>()
                val user = createUserFromUserInfo(userInfo)
                user?.let { dao.user(user.uuid!!) ?: run { dao.addNewUser(user) } }
                call.respondRedirect("/")
            } else {
                val errorResponse: String = response.bodyAsText()
                call.respondText("Error response from Google.user request: $errorResponse")
            }
        }
    }
    get("/logout") {
        call.sessions.clear<UserSession>()
        call.respondText("Logged out!")
    }
}

/**
 * Configures the routes for authentication.
 *
 * @receiver The Routing on which to configure the routes.
 */
fun Route.configureAuthRoutes() {
    authRoutes()
    get("/hello") {
        val userSession: UserSession? = call.sessions.get()
        userSession?.let {
            val response: HttpResponse =
                httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers { append(HttpHeaders.Authorization, "Bearer ${userSession.token}") }
                }
            if (response.status == HttpStatusCode.OK) {
                val userInfo: UserInfo = response.body<UserInfo>()
                call.respondText("This is your userInfo: $userInfo!")
            } else {
                val errorResponse: String = response.bodyAsText()
                call.respondText("Error response: $errorResponse")
            }
        }
            ?: run {
                val redirectUrl =
                    URLBuilder("https://localhost:8443/login").run {
                        parameters.append("redirectUrl", call.request.uri)
                        build()
                    }
                call.respondRedirect(redirectUrl)
            }
    }
}
