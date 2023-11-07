package edu.umass.plugins

import edu.umass.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        singlePageApplication {
            react("my-app/out")
        }
        get("/test") {
            call.respond(mapOf("review_table" to dao.allReviews()))
        }
    }
}
