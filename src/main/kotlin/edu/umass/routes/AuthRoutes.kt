/**
 * This file contains the routes for authorization.
 *
 * @file AuthRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.models.UserInfo
import edu.umass.models.UserSession
import edu.umass.plugins.httpClient
import edu.umass.plugins.redirects
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.headers
import io.ktor.server.application.call
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.request.uri
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
        get("/login") { call.respondRedirect("/callback") }

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
            call.sessions.set(UserSession(principal!!.state!!, principal.accessToken))
            val redirect = redirects[principal.state!!]
            call.respondRedirect(redirect!!)
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
    get("/{path}") {
        val userSession: UserSession? = call.sessions.get()
        userSession?.let {
            val tokenInfoResponse: HttpResponse =
                httpClient.get(
                    "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=${userSession.token}",
                )
            if (tokenInfoResponse.status != HttpStatusCode.OK) {
                val response: HttpResponse =
                    httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                        headers { append(HttpHeaders.Authorization, "Bearer ${userSession.token}") }
                    }
                if (response.status == HttpStatusCode.OK) {
                    val userInfo: UserInfo = response.body<UserInfo>()
                    call.respondText("Hello, ${userInfo.name}!")
                }
            } else {
                call.respondText("Invalid or expired access token. Please log in again.")
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
