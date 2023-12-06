package edu.umass.routes

import edu.umass.models.Review
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

class ReviewRoutesTest {
    // Dummy objects
    val newReviewJson = buildString {
        append("{")
        append("\"id\": 5,")

        append("\"Professor\": {")
        append("\"id\": 5,")
        append("\"firstName\": \"Subhransu\",")
        append("\"lastName\": \"Maji\"")
        append("},")

        append("\"Course\": {")
        append("\"cicsId\": 201,")
        append("\"name\": \"Test Course\",")
        append("\"description\": \"This is a test course\",")
        append("\"credits\": 3,")
        append("\"courseLevel\": 200")
        append("},")

        append("\"difficulty\": 5,")
        append("\"quality\": 4,")
        append("\"comment\": \"Teacher cares about her students\",")
        append("\"fromRmp\": false,")
        append("\"forCredit\": true,")
        append("\"attendance\": true,")
        append("\"textbook\": false,")
        append("}")
    }
    val editReviewJson = buildString {
        append("{")
        append("\"id\": 5,")

        append("\"Professor\": {")
        append("\"id\": 5,")
        append("\"firstName\": \"Subhransu\",")
        append("\"lastName\": \"Maji\"")
        append("},")

        append("\"Course\": {")
        append("\"cicsId\": 301,")
        append("\"name\": \"Edited Test Course\",")
        append("\"description\": \"This is a test course\",")
        append("\"credits\": 3,")
        append("\"courseLevel\": 200")
        append("},")

        append("\"difficulty\": 5,")
        append("\"quality\": 4,")
        append("\"comment\": \"Teacher cares about her students\",")
        append("\"fromRmp\": false,")
        append("\"forCredit\": true,")
        append("\"attendance\": true,")
        append("\"textbook\": false,")
        append("}")
    }

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
        val response = client.get("/review/2") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)

        val review: Review = decodeFromString(response.bodyAsText())
        assertTrue(review.id == 2, "Id should be 2, was ${review.id}")
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
