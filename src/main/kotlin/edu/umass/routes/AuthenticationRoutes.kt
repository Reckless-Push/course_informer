/**
 * This file contains the routes for the authentication flow.
 *
 * @file ProfessorRoutes.kt
 * @version 0.1
 */
package edu.umass.routes

// import io.ktor.server.sessions.set

import edu.umass.plugins.UserInfo
import edu.umass.plugins.UserSession
import io.ktor.client.*
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpHeaders
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.*
import kotlinx.html.*

// import io.ktor.server.response.redirects
// import io.ktor.server.response.respondRedirect
// import io.ktor.server.routing.*
// import io.ktor.server.request.*
// import kotlinx.serialization.*

/** Route to authenticate a user via delegated oauth. */
fun Route.authenticationRoutes(httpClient: HttpClient) {
    authenticate("auth-oauth-google") {
        get("/login") {
            // Redirects to 'authorizeUrl' automatically
        }

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
            call.sessions.set(UserSession(principal!!.state!!, principal.accessToken))
            val redirect = redirects[principal.state!!]
            call.respondRedirect(redirect!!)
        }
    }
    get("/") { call.respondHtml { body { p { a("/login") { +"Login with Google" } } } } }
    get("/{path}") {
        val userSession: UserSession? = call.sessions.get()
        if (userSession != null) {
            val userInfo: UserInfo =
                    httpClient
                            .get("https://www.googleapis.com/oauth2/v2/userinfo") {
                                headers {
                                    append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
                                }
                            }
                            .body()
            call.respondText("Hello, ${userInfo.name}!")
        } else {
            val redirectUrl =
                    URLBuilder("http://0.0.0.0:8080/login").run {
                        parameters.append("redirectUrl", call.request.uri)
                        build()
                    }
            call.respondRedirect(redirectUrl)
        }
    }
}
