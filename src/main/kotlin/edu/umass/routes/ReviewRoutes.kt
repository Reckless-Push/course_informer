/**
 * This file contains the routes for the Review table.
 *
 * @file ReviewRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.Review
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

/**
 * Defines the routes for the Review table.
 *
 * @receiver The Route on which to define the routes.
 */
fun Route.reviewRoutes() {
    listReviews()
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
            // Receiving a Review object instead of Equipment
            val review: Review = call.receive<Review>()
            val newReview = dao.addNewReview(review)

            // Responding with the ID of the new review if it's successfully created
            newReview?.let { call.respondText("${it.id}", status = HttpStatusCode.Created) }
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
