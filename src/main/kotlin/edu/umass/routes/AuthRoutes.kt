/**
 * This file contains the routes for authorization.
 *
 * @file AuthRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.models.UserSession
import io.ktor.server.application.call
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

/**
 * Configures the routes for authentication.
 *
 * @receiver The Routing on which to configure the routes.
 */
fun Routing.configureAuthRoutes() {
    authenticate("auth-oauth-google") {
        get("login") { call.respondRedirect("/callback") }

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
            call.sessions.set(UserSession(principal?.accessToken.toString()))
            call.respondRedirect("/hello")
        }
    }
}
