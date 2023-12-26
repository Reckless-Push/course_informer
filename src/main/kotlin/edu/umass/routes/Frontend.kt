/**
 * This file contains the frontend route.
 *
 * @file Frontend.kt
 * @version 0.1
 */

package edu.umass.routes

import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.Route

/**
 * Route to serve a React single-page application.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.frontend() {
    singlePageApplication {
        useResources = true
        defaultPage = "index.html" // Default file to serve
        filesPath = "static" // Folder containing the static files
    }
    staticResources("/html", "documentation/html", "index.html")
}
