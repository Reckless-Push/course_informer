/**
 * This file contains the code for configuring OAuth.
 *
 * @file ConfigureOauth.kt
 * @version 0.1
 */

package edu.umass.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings.OAuth2ServerSettings
import io.ktor.server.auth.oauth

val httpClient =
    HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(Logging) { level = LogLevel.INFO }
    }

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
            defaultScopes =
                listOf(
                    "https://www.googleapis.com/auth/userinfo.profile",
                    "https://www.googleapis.com/auth/userinfo.email",
                ),
        )

    // HttpClient instantiation
    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "${System.getenv("BASE_URL")}/callback" }
            providerLookup = { oauthSettings }
            client = httpClient
        }
    }
}
