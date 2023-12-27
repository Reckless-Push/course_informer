package edu.umass.routes

import edu.umass.models.Course
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.encodeToJsonElement

class CourseRoutesTest {
    /** Test to verify the GET request for courses. */
    @Test
    fun testGetCourse() = testApplication {
        val response = client.get("/course") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        val courses: Map<String, List<Course>> = decodeFromString(response.bodyAsText())
        assertTrue(courses["course_table"]?.isNotEmpty() ?: false, "Courses list should not be empty")
    }

    /** Test to verify the POST request for creating a new course. */
    @Test
    fun testPostCourse() = testApplication {
        val courseJson =
            Json.encodeToJsonElement(
                Course(
                    id = 2,
                    cicsId = "201",
                    courseLevel = 200,
                    department = "CICS",
                    name = "Test Course",
                    description = "This is a test course",
                    credits = 3,
                ),
            )
                .toString()

        val response =
            client.post("/course") {
                url { protocol = URLProtocol.HTTPS }
                contentType(ContentType.Application.Json)
                setBody(courseJson)
            }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    /** Test to verify the GET request for a specific course by ID. */
    @Test
    fun testGetCourseId() = testApplication {
        val response = client.get("/course/1") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        val course: Course = decodeFromString(response.bodyAsText())
        assertTrue(course.cicsId == "101", "Course id should be 101")
    }

    /** Test to verify the POST request for updating a course by ID. */
    @Test
    fun testPostCourseId() = testApplication {
        val editCourseJson = buildString {
            append("{")
            append("\"id\": 2,")
            append("\"cicsId\": \"101\",")
            append("\"department\": \"CICS\",")
            append("\"name\": \"Renamed Test Course\",")
            append("\"description\": \"Renamed test course description\",")
            append("\"credits\": 3,")
            append("\"courseLevel\": 100")
            append("}")
        }

        val response =
            client.post("/course/1") {
                url { protocol = URLProtocol.HTTPS }
                contentType(ContentType.Application.Json)
                setBody(editCourseJson)
            }
        assertEquals(HttpStatusCode.OK, response.status)

        val isEdited: Boolean = decodeFromString(response.bodyAsText())
        assertTrue(isEdited, "Course should be edited")
    }

    /** Test to verify the GET request for deleting a course by ID. */
    @Test
    fun testGetCourseDeleteId() = testApplication {
        val response = client.get("/course/delete/2") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.Accepted, response.status)
    }
}
