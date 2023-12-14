package edu.umass.routes

import edu.umass.models.Course
import edu.umass.models.LetterGrade
import edu.umass.models.Professor
import edu.umass.models.Review
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString

class ReviewRoutesTest {
    // Dummy objects
    val prof = Professor(9, "Subhransu", "Maji")
    val course = Course(501, "Test Course", "This is a test course", 3, 100)
    val uuid = UUID.fromString("4472068d-c076-4ca0-b9de-085c0a4c7a14")
    val user =
        User(
            uuid,
            "Alice",
            "Smith",
            "alice@example.com",
        )
    val datetime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val newReview =
        Review(
            11,
            prof,
            course,
            uuid,
            datetime,
            5,
            4,
            "Comment",
            false,
            true,
            true,
            false,
            LetterGrade.GRADE_A,
        )
    val editReview =
        Review(
            2,
            prof,
            course,
            uuid,
            datetime,
            5,
            4,
            "Other comment",
            false,
            true,
            true,
            false,
            LetterGrade.GRADE_A,
        )
    val newReviewJson = Json.encodeToString(newReview)
    val editReviewJson = Json.encodeToString(editReview)

    /** Test to verify the GET request for reviews. */
    @Test
    fun testGetReviews() = testApplication {
        val response = client.get("/review") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        val reviews: Map<String, List<Review>> = decodeFromString(response.bodyAsText())
        assertTrue(reviews["review_table"]?.isNotEmpty() ?: false, "Reviews list should not be empty")
    }

    /** Test to verify the POST request for creating a new review. */
    @Test
    fun testPostReview() = testApplication {
        val response =
            client.post("/review") {
                url { protocol = URLProtocol.HTTPS }
                contentType(ContentType.Application.Json)
                setBody(newReviewJson)
            }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    /** Test to verify the GET request for a specific review by ID. */
    @Test
    fun testGetReviewId() = testApplication {
        val response = client.get("/review/1") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        val review: Review = decodeFromString(response.bodyAsText())
        assertTrue(review.id == 1, "Id should be 1, was ${review.id}")
    }

    /** Test to verify the POST request for updating a review by ID. */
    @Test
    fun testPostReviewId() = testApplication {
        val response =
            client.post("/review/update/2") {
                url { protocol = URLProtocol.HTTPS }
                contentType(ContentType.Application.Json)
                setBody(editReviewJson)
            }
        assertEquals(HttpStatusCode.OK, response.status)

        val isEdited: Boolean = decodeFromString(response.bodyAsText())
        assertTrue(isEdited, "Review should be edited")
    }

    /** Test to verify the GET request for deleting a review by ID. */
    @Test
    fun testGetReviewDeleted() = testApplication {
        val response = client.get("/review/delete/1") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.Accepted, response.status)
    }
}
