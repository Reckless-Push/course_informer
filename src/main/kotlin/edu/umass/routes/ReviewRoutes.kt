/**
 * This file contains the routes for the Review table.
 *
 * @file ReviewRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.Review
import edu.umass.models.User
import edu.umass.models.UserSession
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import java.util.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Defines the routes for the Review table.
 *
 * @receiver The Route on which to define the routes.
 */
fun Route.reviewRoutes() {
    listReviews()
    listUserReviews()
    listCourseReviews()
    listProfessorReviews()
    getReview()
    addReview()
    updateReview()
    deleteReview()
}

/**
 * Route to list all reviews.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.listReviews() {
    get("/review") { call.respond(mapOf("review_table" to dao.allReviews())) }
}

/**
 * Route to list all the current users reviews.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.listUserReviews() {
    get("/review/user") {
        val userSession: UserSession? = call.sessions.get()
        userSession
            ?: run {
                call.respond(HttpStatusCode.Unauthorized, "User not logged in")
                return@get
            }

        val userInfo = getUserInfoFromSession(userSession)

        call.respond(
            mapOf(
                "review_table" to dao.allUserReviews(UUID.nameUUIDFromBytes(userInfo.id.toByteArray())),
            ),
        )
    }
}

/**
 * Route to list all reviews for a given course.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.listCourseReviews() {
    get("/review/course/{cicsId}") {
        val cicsId = call.parameters["cicsId"]?.toIntOrNull()
        cicsId
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed cicsId")
                return@get
            }

        call.respond(mapOf("review_table" to dao.allCourseReviews(cicsId)))
    }
}

/**
 * Route to list all reviews for a given professor.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.listProfessorReviews() {
    get("/review/professor/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }

        call.respond(mapOf("review_table" to dao.allProfessorReviews(id)))
    }
}

/**
 * Route to get a review by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getReview() {
    get("/review/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }

        val review = dao.review(id)
        review?.let { call.respond(review) }
            ?: call.respond(HttpStatusCode.NotFound, "No review with id $id")
    }
}

/**
 * Route to add a new review.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.addReview() {
    post("/review") {
        try {
            val review: Review = call.receive<Review>()
            val userSession: UserSession? = call.sessions.get()
            val user =
                dao.user(UUID.nameUUIDFromBytes(getUserInfoFromSession(userSession!!).id.toByteArray()))
            val newReview = createAndPersistReview(review, user)
            newReview?.let { call.updateAndRespond(user, newReview) }
                ?: call.respond(HttpStatusCode.InternalServerError)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid input")
        }
    }
}

/**
 * Route to update a review by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.updateReview() {
    post("/review/update/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@post
            }
        try {
            // Receiving a Review object instead of Equipment
            val review: Review = call.receive<Review>()
            val updatedReview = dao.editReview(review, id)
            updatedReview.let {
                if (updatedReview) {
                    call.respondText("Review edited correctly", status = HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid input")
        }
    }
}

/**
 * Route to delete a review by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.deleteReview() {
    get("/review/delete/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }
        val review = dao.deleteReview(id)
        if (review) {
            call.respondText("Deleted review $id", status = HttpStatusCode.Accepted)
        } else {
            call.respond(HttpStatusCode.NotFound, "No review with id $id")
        }
    }
}

private suspend fun ApplicationCall.updateAndRespond(
    user: User?,
    newReview: Review,
) {
    val updatedUser =
        user?.copy(
            reviews = user.reviews + newReview,
        ) ?: return

    dao.editUser(updatedUser, updatedUser.uuid!!)
    respondText("${newReview.id}", status = HttpStatusCode.Created)
}

/**
 * Creates and persists a review.
 *
 * @param review The review to create and persist.
 * @param user The user to associate with the review.
 * @return The created review.
 */
private suspend fun createAndPersistReview(
    review: Review,
    user: User?,
): Review? =
    user?.let { currentUser ->
        dao.addNewReview(
            Review(
                professor = review.professor,
                course = review.course,
                userId = currentUser.uuid,
                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                difficulty = review.difficulty,
                quality = review.quality,
                comment = review.comment,
                fromRmp = review.fromRmp,
                forCredit = review.forCredit,
                attendance = review.attendance,
                textbook = review.textbook,
                grade = review.grade,
            ),
        )
    }
