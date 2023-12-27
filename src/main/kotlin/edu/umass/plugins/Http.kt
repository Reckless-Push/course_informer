/**
 * Configures HTTP security features including CORS, HSTS, and HTTPS redirection for the Ktor
 * application.
 *
 * @file Http.kt
 * @version 0.1
 */

package edu.umass.plugins

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.hsts.HSTS
import io.ktor.server.plugins.httpsredirect.HttpsRedirect

/**
 * Configures HTTP security features including CORS, HSTS, and HTTPS redirection for the Ktor
 * application.
 *
 * @receiver The Application on which to install security features.
 */
fun Application.configureHttp() {
    // Install and configure CORS (Cross-Origin Resource Sharing) to allow web applications from other
    // domains to interact with the server.
    install(CORS) {
        // Permit specific HTTP methods from cross-origin requests.
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        // Allow specific headers from cross-origin requests.
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowNonSimpleContentTypes = true // Allow all content types
        allowCredentials = true // Allow credentials
        allowSameOrigin = true // Allow requests from the same origin

        // Allow requests from any host. Note: This should be configured with caution in a production
        // environment.
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    // If we're deploying to digital ocean, we don't need to enforce HTTPS.
    if (System.getenv("ENVIRONMENT") != "digitalocean") {
        // Install and enforce the HSTS (HTTP Strict Transport Security) policy, which instructs
        // browsers to only use HTTPS.
        install(HSTS) {
            // Apply HSTS policy to all subdomains as well.
            includeSubDomains = true
        }

        // Install and configure automatic redirection from HTTP to HTTPS.
        install(HttpsRedirect) {
            // Define the HTTPS port to redirect to, default is the standard HTTPS port (443).
            sslPort = System.getenv("SSL_PORT").toInt()

            // Specify the type of redirect, 301 (permanent) or 302 (found/temporary).
            permanentRedirect = true
        }
    }
}
