/**
 * This file contains the routes for authorization.
 *
 * @file AuthRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.models.UserSession
import edu.umass.plugins.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.server.application.call
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
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
fun Routing.authRoutes() {
    authenticate("auth-oauth-google") {
        get("/login") {}

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
            call.sessions.set(UserSession(principal!!.state!!, principal.accessToken))
            call.respondRedirect("/hello")
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
fun Routing.configureAuthRoutes() {
    authRoutes()
    get("/hello") {
        val userSession: UserSession? = call.sessions.get()
        val userInfo: String =
            httpClient
                .get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers { append(HttpHeaders.Authorization, "Bearer ${userSession!!.token}") }
                }
                .body()
        call.respondText("This is your userInfo: $userInfo")
    }
}
