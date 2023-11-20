/**
 * This file contains the code for configuring OAuth.
 *
 * @file ConfigureOauth.kt
 * @version 0.1
 */

package edu.umass.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings.OAuth2ServerSettings
import io.ktor.server.auth.oauth

val redirects: MutableMap<String, String> = mutableMapOf()
val httpClient = HttpClient(CIO)

/**
 * Configures OAuth2 authentication for the Ktor application.
 *
 * @receiver The Application on which to install security features.
 */
fun Application.configureOauth() {
    val oauthSettings =
        OAuth2ServerSettings(
            name = "google",
            authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
            accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
            requestMethod = HttpMethod.Post,
            clientId = System.getenv("GOOGLE_CLIENT_ID"),
            clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
            defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
            onStateCreated = { call, state ->
                redirects[state] = call.request.queryParameters["redirectUrl"] ?: "/"
            },
        )

    // HttpClient instantiation
    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "https://localhost:8443/callback" }
            providerLookup = { oauthSettings }
            client = httpClient
        }
    }
}
