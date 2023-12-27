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
import io.ktor.server.routing.get
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Deploy")

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
    get("/update") {
        try {
            val process = ProcessBuilder("bash", "deploy.sh").start()
            val deployScriptOutput = process.inputStream.bufferedReader().use { it.readText() }
            val deployScriptError = process.errorStream.bufferedReader().use { it.readText() }
            logger.info("Deploy Script output: $deployScriptOutput")
            logger.error("Deploy Script error: $deployScriptError")
        } catch (e: Exception) {
            "Error running script: ${e.message}"
        }
    }
}
