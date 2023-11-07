package edu.umass.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

/**
 * Configures JSON serialization for the Ktor application.
 *
 * @receiver The Application on which to install and configure content negotiation.
 */
fun Application.configureSerialization() {
    // Install the ContentNegotiation feature to handle serialization.
    install(ContentNegotiation) {
        // Setup JSON as the default content negotiation format.
        json()
    }
}
