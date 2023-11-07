package edu.umass.plugins

import edu.umass.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
        // Serve a React single-page application from the specified path.
        singlePageApplication {
            react("my-app/out")
        }
        // Define a GET endpoint to retrieve all reviews from the database.
        get("/test") {
            // Respond with a JSON object containing the review table data.
            call.respond(mapOf("review_table" to dao.allReviews()))
        }
    }
}
