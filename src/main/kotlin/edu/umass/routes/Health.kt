/**
 * Health check route.
 *
 * @file Health.kt
 * @version 1.0
 */

package edu.umass.routes

import io.ktor.http.ContentType
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

/**
 * Route to check the health of the application.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.healthCheck() {
    get("/health") { call.respondText("OK", ContentType.Text.Plain) }
}
