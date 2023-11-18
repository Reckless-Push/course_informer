/**
 * Configures serialization for the Ktor application.
 *
 * @file Serialization.kt
 * @version 0.1
 */

package edu.umass.plugins

import io.ktor.client.engine.cio.CIO
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

/**
 * Configures JSON serialization for the Ktor application.
 *
 * @receiver The Application on which to install and configure content negotiation.
 */
fun Application.configureSerialization() {
    // Install the ContentNegotiation feature to handle serialization.
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            // Setup JSON as the default content negotiation format.
            json()
        }
    }
}
