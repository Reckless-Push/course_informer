/**
 * This file contains the code for configuring the user session.
 *
 * @file ConfigureSessions.kt
 * @version 0.1
 */

package edu.umass.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

/**
 * Configures sessions for the Ktor application.
 *
 * @receiver The Application on which to install security features.
 */
fun Application.configureSessions() {
    /** @property count */
    data class MySession(val count: Int = 0)
    install(Sessions) { cookie<MySession>("MY_SESSION") { cookie.extensions["SameSite"] = "lax" } }
}
