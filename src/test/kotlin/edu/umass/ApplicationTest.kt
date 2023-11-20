package edu.umass

import edu.umass.plugins.configureOauth
import edu.umass.plugins.configureRouting
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureOauth()
            configureRouting()
        }
        client.get("/").apply { assertEquals(HttpStatusCode.OK, status) }
    }
}
