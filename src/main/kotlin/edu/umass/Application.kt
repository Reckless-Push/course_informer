/** Application file for the Ktor server. */

package edu.umass

import edu.umass.dao.DatabaseFactory
import edu.umass.plugins.configureHttp
import edu.umass.plugins.configureRouting
import edu.umass.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory

import java.io.FileNotFoundException
import java.io.InputStream
import java.security.KeyStore

const val DEFAULT_KTOR_PORT = 8080
const val DEFAULT_KTOR_SSL_PORT = 8443

/**
 * Defines the module for the Ktor application, configuring the necessary plugins and database
 * initialization.
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
    configureHttp()

    // Initialize the database connection and schema.
    DatabaseFactory.init()
}

/** The main entry point of the Ktor server application. */
fun main() {
    // Getting the alias and passwords from environment variables
    val keyAlias = System.getenv("KEY_ALIAS") ?: "defaultAlias"
    val keyStorePassword = System.getenv("KEYSTORE_PASSWORD") ?: "defaultPassword"
    val privateKeyPassword = System.getenv("PRIVATE_KEY_PASSWORD") ?: "defaultPassword"

    // Load the keystore from the resources folder
    val keyStoreStream: InputStream =
        Thread.currentThread().contextClassLoader.getResourceAsStream("keystore.jks")
            ?: throw FileNotFoundException("Keystore file not found in resources")
    val keyStore =
        KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(keyStoreStream, keyStorePassword.toCharArray())
        }

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector { port = DEFAULT_KTOR_PORT }
        sslConnector(
            keyStore = keyStore,
            keyAlias = keyAlias,
            keyStorePassword = { keyStorePassword.toCharArray() },
            privateKeyPassword = { privateKeyPassword.toCharArray() },
        ) {
            port = DEFAULT_KTOR_SSL_PORT
        }
        module(Application::module)
    }

    embeddedServer(Netty, environment).start(wait = true)
}
