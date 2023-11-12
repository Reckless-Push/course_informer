package edu.umass

import edu.umass.dao.DatabaseFactory
import edu.umass.plugins.configureHTTP
import edu.umass.plugins.configureRouting
import edu.umass.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // configureSecurity()
    configureSerialization()
    configureRouting()
    configureHTTP()
    DatabaseFactory.init()
}
