/**
 * This file contains the routes for the Course table.
 *
 * @file UserRoutes.kt
 * @version 0.1
 */

package edu.umass.routes

import edu.umass.dao.dao
import edu.umass.models.Course
import edu.umass.models.CourseFilter
import edu.umass.models.CourseIngest
import edu.umass.models.ExtractedCourse

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.slf4j.LoggerFactory

import java.io.File
import java.util.UUID

import kotlinx.serialization.json.Json

private val logger = LoggerFactory.getLogger("Extractor")

/**
 * Defines the routes for the Course table.
 *
 * @receiver The Route on which to define the routes.
 */
fun Route.courseRoutes() {
    listCourses()
    ingestCourses()
    getCourse()
    getFilteredCourses()
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
 * Route to ingest courses.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.ingestCourses() {
    post("/course/ingest") {
        val request: CourseIngest = call.receive<CourseIngest>()
        val url = Url(request.url)
        val filePath = "${UUID.randomUUID()}.txt"

        if (downloadPdf(url, filePath)) {
            val uuid = runPythonScript(filePath)
            val jsonFilePath = "$uuid.json"
            val jsonContent = File(jsonFilePath).readText()
            val extractedCourses: List<ExtractedCourse> = Json.decodeFromString(jsonContent)
            call.respond(mapOf("extracted_courses" to extractedCourses))

            // Delete the files after loading the extracted course list
            val jsonFile = File(jsonFilePath)
            if (jsonFile.exists()) {
                jsonFile.delete()
            }
            val pdfFile = File(filePath)
            if (pdfFile.exists()) {
                pdfFile.delete()
            }
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to download or process the file")
        }
    }
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
 * Route to get a course by ID.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getFilteredCourses() {
    post("/course/filter") {
        val filter: CourseFilter = call.receive<CourseFilter>()
        call.respond(mapOf("course_table" to dao.filteredCourses(filter)))
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

/**
 * Downloads a PDF from a URL and saves it to a file.
 *
 * @param url The URL of the PDF to download.
 * @param fileName The name of the file to save the PDF to.
 * @return True if the download was successful, False otherwise.
 */
suspend fun downloadPdf(
    url: Url,
    fileName: String,
): Boolean =
    try {
        val client = HttpClient()
        val response: HttpResponse = client.get(url)
        val bytes = response.readBytes()
        File(fileName).writeBytes(bytes)
        client.close()
        true
    } catch (e: Exception) {
        false
    }

/**
 * Runs a Python script on a PDF file.
 *
 * @param filePath The path to the PDF file to run the script on.
 * @return The output of the script.
 */
fun runPythonScript(filePath: String): String =
    try {
        val outputId = UUID.randomUUID().toString()
        val process = ProcessBuilder("python", "extractor.py", filePath, "$outputId.json").start()
        val scriptOutput = process.inputStream.bufferedReader().use { it.readText() }
        val scriptError = process.errorStream.bufferedReader().use { it.readText() }
        logger.info("Script output: $scriptOutput")
        logger.error("Script error: $scriptError")
        outputId
    } catch (e: Exception) {
        "Error running script: ${e.message}"
    }
