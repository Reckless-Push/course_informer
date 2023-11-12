package edu.umass

import edu.umass.dao.DatabaseFactory
import edu.umass.plugins.configureHTTP
import edu.umass.plugins.configureRouting
import edu.umass.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/**
 * The main entry point of the Ktor server application.
 */
fun main() {
    // Start an embedded server using Netty as the application engine on port 8080 and accepting connections on all network interfaces.
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true) // Start the server and wait for it to finish initializing before continuing to the next line of code.
}

/**
 * Defines the module for the Ktor application, configuring the necessary plugins and database initialization.
 *
 * @receiver The Application on which to configure the module.
 */
fun Application.module() {
    // configureSecurity()

    // Set up JSON serialization for the application.
    configureSerialization()

    // Set up the routing for the application, defining endpoints and their behavior.
    configureRouting()

    // Configure HTTP security features such as CORS and HTTPS redirection.
    configureHTTP()

    // Initialize the database connection and schema.
    DatabaseFactory.init()
}
