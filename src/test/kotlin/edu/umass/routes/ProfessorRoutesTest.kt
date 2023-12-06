package edu.umass.routes

import edu.umass.models.Professor
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.serialization.json.Json.Default.decodeFromString

typealias ProfList = List<Professor>

class ProfessorRoutesTest {
    val newProfessorJson = buildString {
        append("{")
        append("\"id\": 5,")
        append("\"firstName\": \"Subhransu\",")
        append("\"lastName\": \"Maji\"")
        append("}")
    }

    /** Test to verify the GET request for professors. */
    @Test
    fun testGetProfessor() = testApplication {
        val response = client.get("/professor") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        // SpotlessApply would not allow Professor as the generic parameter due to length constraint
        val professors: Map<String, ProfList> = decodeFromString(response.bodyAsText())
        assertTrue(
            professors["professor_table"]?.isNotEmpty() ?: false,
            "Professors list should not be empty",
        )
    }

    /** Test to verify the POST request for creating a new professor. */
    @Test
    fun testPostProfessor() = testApplication {
        val response =
            client.post("/professor") {
                url { protocol = URLProtocol.HTTPS }
                contentType(ContentType.Application.Json)
                setBody(newProfessorJson)
            }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    /** Test to verify the GET request for a specific professor by ID. */
    @Test
    fun testGetProfessorId() = testApplication {
        val response = client.get("/professor/5") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        val professor: Professor = decodeFromString(response.bodyAsText())
        assertTrue(professor.id == 5, "Id should be 5")
    }

    /** Test to verify the POST request for updating a professor by ID. */
    @Test
    fun testPostProfessorId() = testApplication {
        val editProfessorJson = buildString {
            append("{")
            append("\"id\": 5,")
            append("\"firstName\": \"Max\",")
            append("\"lastName\": \"Hamilton\"")
            append("}")
        }
        val responseEdit =
            client.post("/professor/update/5") {
                url { protocol = URLProtocol.HTTPS }
                contentType(ContentType.Application.Json)
                setBody(editProfessorJson)
            }
        assertEquals(HttpStatusCode.OK, responseEdit.status)

        val isEdited: Boolean = decodeFromString(responseEdit.bodyAsText())
        assertTrue(isEdited, "Professor should be edited")
    }

    /** Test to verify the GET request for deleting a professor by ID. */
    @Test
    fun testGetProfessorDeleted() = testApplication {
        val response = client.get("/professor/delete/3") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.Accepted, response.status)
    }
}
