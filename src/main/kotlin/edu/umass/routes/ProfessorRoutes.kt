/**
 * This file contains the routes for the Professor table.
 *
 * @file ProfessorRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.Professor
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

/**
 * Route to list all professors.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.listProfessors() {
    get("/professor") { call.respond(mapOf("professor_table" to dao.allProfessors())) }
}

/**
 * Route to get a professor by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getProfessor() {
    get("/professor/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }

        val professor = dao.professor(id)
        professor?.let { call.respond(professor) }
            ?: call.respond(HttpStatusCode.NotFound, "No professor with id $id")
    }
}

/**
 * Route to add a new review.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.addProfessor() {
    post("/professor") {
        try {
            // Receiving a Review object instead of Equipment
            val professor: Professor = call.receive<Professor>()
            val newProfessor = dao.addNewProfessor(professor)

            // Responding with the ID of the new review if it's successfully created
            newProfessor?.let { call.respondText("${it.id}", status = HttpStatusCode.Created) }
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
fun Route.updateProfessor() {
    post("/professor/update/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@post
            }
        try {
            // Receiving a Review object instead of Equipment
            val professor: Professor = call.receive<Professor>()
            val updatedProfessor = dao.editProfessor(professor, id)
            updatedProfessor.let {
                if (updatedProfessor) {
                    call.respondText("Professor edited correctly", status = HttpStatusCode.Accepted)
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
fun Route.deleteProfessor() {
    get("/professor/delete/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }
        val professor = dao.deleteProfessor(id)
        if (professor) {
            call.respondText("Deleted professor $id", status = HttpStatusCode.Accepted)
        } else {
            call.respond(HttpStatusCode.NotFound, "No review with id $id")
        }
    }
}
