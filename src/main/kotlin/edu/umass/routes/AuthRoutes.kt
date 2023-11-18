/**
 * This file contains the routes for the Professor table.
 *
 * @file ProfessorRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.Professor
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

/**
 * Route to authenticate a user via delegated oauth.
 */
fun Route.authenticate("auth-oauth-google") {
    get("/login") {
        // Redirects to 'authorizeUrl' automatically
    }

    get("/callback") {
        val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
        call.sessions.set(UserSession(principal!!.state!!, principal.accessToken))
        val redirect = redirects[principal.state!!]
        call.respondRedirect(redirect!!)
    }
    get("/") {
        call.respondHtml {
            body {
                p {
                    a("/login") { +"Login with Google" }
                }
            }
        }
    }
    get("/{path}") {
        val userSession: UserSession? = call.sessions.get()
        if (userSession != null) {
            val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
                }
            }.body()
            call.respondText("Hello, ${userInfo.name}!")
        } else {
            val redirectUrl = URLBuilder("http://0.0.0.0:8080/login").run {
                parameters.append("redirectUrl", call.request.uri)
                build()
            }
            call.respondRedirect(redirectUrl)
        }
    }
}