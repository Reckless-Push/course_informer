/**
 * This file contains the code for configuring OAuth2 authentication for the Ktor application. It
 * also contains the code for incrementing the session count.
 *
 * @since 0.1.0
 * @file This files defines the Security.kt file.
 */

package edu.umass.plugins

import io.ktor.server.application.Application

/**
 * Configures OAuth2 authentication for the Ktor application.
 *
 * @receiver The Application on which to install security features.
 */
fun Application.configureSecurity() {
    // configureOauth()
    configureSessions()
}
