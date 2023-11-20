/**
 * Configures the user authentication for the Ktor application.
 *
 * @file Authentication.kt
 * @version 0.1
 */
package edu.umass.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.oauth
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class UserSession(val state: String, val token: String)

@Serializable
data class UserInfo(
        val id: String,
        val name: String,
        @SerialName("given_name") val givenName: String,
        @SerialName("family_name") val familyName: String,
        val picture: String,
        val locale: String
)

/**
 * Configures JSON serialization for the Ktor application.
 *
 * @receiver The Application on which to install and configure content negotiation.
 */
fun Application.configureHttpClient(): HttpClient {
    // Install the ContentNegotiation feature to handle serialization.
    val HttpClient = HttpClient(CIO) {}
    return HttpClient
}

fun Application.configureSessions() {
    install(Sessions) { cookie<UserSession>("user_session") }
}

fun Application.configureAuthentication(): HttpClient {
    val httpClient = configureHttpClient()
    val redirects = mutableMapOf<String, String>()
    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                        name = "google",
                        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                        accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                        requestMethod = HttpMethod.Post,
                        clientId = System.getenv("GOOGLE_CLIENT_ID"),
                        clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                        defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                        extraAuthParameters = listOf("access_type" to "offline"),
                        onStateCreated = { call, state ->
                            redirects[state] = call.request.queryParameters["redirectUrl"]!!
                        },
                )
            }
            client = httpClient
        }
    }
    return httpClient
}
