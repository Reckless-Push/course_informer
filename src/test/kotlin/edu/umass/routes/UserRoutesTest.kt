package edu.umass.routes

import edu.umass.models.User
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString

class UserRoutesTest {
    // Dummy objects
    val uuid = UUID.fromString("4472068d-c076-4ca0-b9de-085c0a4c7a14")
    val existingUser =
        User(
            uuid,
            "Alice",
            "Smith",
            "alice@example.com",
        )
    val editUser =
        User(
            uuid,
            "Alica",
            "Jones",
            "alica@example.com",
        )
    val existingUserJson = Json.encodeToString(existingUser)
    val editUserJson = Json.encodeToString(editUser)

    /** Test to verify the GET request for users. */
    @Test
    fun testGetUsers() = testApplication {
        val response = client.get("/user") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        val users: Map<String, List<User>> = decodeFromString(response.bodyAsText())
        assertTrue(users["user_table"]?.isNotEmpty() ?: false, "Users list should not be empty")
    }

    /** Test to verify the GET request for a specific user by UUID. */
    @Test
    fun testGetUserId() = testApplication {
        val response =
            client.get("/user/4472068d-c076-4ca0-b9de-085c0a4c7a14") {
                url { protocol = URLProtocol.HTTPS }
            }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    /** Test to verify the POST request for updating a user by UUID. */
    @Test
    fun testPostUserId() = testApplication {
        val response =
            client.post("/user/4472068d-c076-4ca0-b9de-085c0a4c7a14") {
                url { protocol = URLProtocol.HTTPS }
                contentType(ContentType.Application.Json)
                setBody(editUserJson)
            }
        assertEquals(HttpStatusCode.OK, response.status)
    }

}
