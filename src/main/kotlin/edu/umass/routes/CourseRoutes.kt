/**
 * This file contains the routes for the Course table.
 *
 * @file UserRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.Course
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

/**
 * Defines the routes for the Course table.
 *
 * @receiver The Route on which to define the routes.
 */
fun Route.courseRoutes() {
    listCourses()
    getCourse()
    addCourse()
    updateCourse()
    deleteCourse()
}

/**
 * Route to list all courses.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.listCourses() {
    get("/course") { call.respond(mapOf("course_table" to dao.allCourses())) }
}

/**
 * Route to get a course by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getCourse() {
    get("/course/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }

        val course = dao.course(id)
        course?.let { call.respond(course) }
            ?: call.respond(HttpStatusCode.NotFound, "No course with id $id")
    }
}

/**
 * Route to add a new course.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.addCourse() {
    post("/course") {
        try {
            // Receiving a Course object instead of Equipment
            val course: Course = call.receive<Course>()
            val newCourse = dao.addNewCourse(course)
            call.respondText("Course $newCourse.cicsId stored correctly", status = HttpStatusCode.Created)
        } catch (e: Exception) {
            call.respondText("Error storing the course", status = HttpStatusCode.InternalServerError)
        }
    }
}

/**
 * Route to update a course.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.updateCourse() {
    post("/course/{id}") {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            id
                ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                    return@post
                }
            val course: Course = call.receive<Course>()
            val updatedCourse = dao.editCourse(course, id)
            updatedCourse.let { call.respond(updatedCourse) }
        } catch (e: Exception) {
            call.respondText(e.toString())
        }
    }
}

/**
 * Route to delete a course by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.deleteCourse() {
    get("/course/delete/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        id
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                return@get
            }
        val course = dao.deleteCourse(id)
        if (course) {
            call.respondText("Deleted course $id", status = HttpStatusCode.Accepted)
        } else {
            call.respond(HttpStatusCode.NotFound, "No course with id $id")
        }
    }
}
