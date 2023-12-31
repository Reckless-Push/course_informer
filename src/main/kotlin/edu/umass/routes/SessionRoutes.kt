/**
 * This file contains the routes for the session.
 *
 * @file SessionRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.models.MySession
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

/** Configures the routes for the session. */
fun Route.configureSessionRoutes() {
    get("/session/increment") {
        val session = call.sessions.get<MySession>() ?: MySession()
        call.sessions.set(session.copy(count = session.count + 1))
        call.respondText("Counter is ${session.count}. Refresh to increment.")
    }
}
