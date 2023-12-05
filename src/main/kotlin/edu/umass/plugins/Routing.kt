/**
 * Configures routing and error handling for the Ktor application.
 *
 * @file Routing.kt
 * @version 0.1
 */

package edu.umass.plugins

import edu.umass.routes.configureAuthRoutes
import edu.umass.routes.configureSessionRoutes
import edu.umass.routes.courseRoutes
import edu.umass.routes.frontend
import edu.umass.routes.healthCheck
import edu.umass.routes.professorRoutes
import edu.umass.routes.reviewRoutes
import edu.umass.routes.userRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing

/**
 * Configures routing and error handling for the Ktor application.
 *
 * @receiver The Application on which to install plugins and configure routing.
 */
fun Application.configureRouting() {
    // Install the StatusPages feature to handle exceptions globally.
    install(StatusPages) {
        // Catch all exceptions, log them, and respond with a 500 Internal Server Error status.
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    // Define the routing for the application.
    routing {
        frontend()
        healthCheck()
        configureAuthRoutes()
        configureSessionRoutes()
        reviewRoutes()
        professorRoutes()
        userRoutes()
        courseRoutes()
    }
}
