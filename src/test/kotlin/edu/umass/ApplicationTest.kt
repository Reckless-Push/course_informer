package edu.umass

import edu.umass.plugins.configureRouting
import io.ktor.server.testing.testApplication
import kotlin.test.Test

class ApplicationTest {
    @Test fun testRoot() = testApplication { application { configureRouting() } }
}
