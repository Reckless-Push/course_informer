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
import edu.umass.models.ExtractedMap
import edu.umass.models.Professor
import edu.umass.models.Semester

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

private const val ONE_HUNDRED = 100
private const val GRAD_CUTOFF = 500

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
    getSemesters()
    getCredits()
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
        val filePath = "data/${UUID.randomUUID()}.pdf"

        if (downloadPdf(url, filePath)) {
            val uuid = runPythonScript(filePath)
            val jsonFilePath = "data/$uuid.json"
            val jsonContent = File(jsonFilePath).readText()
            val extractedMap: ExtractedMap = Json.decodeFromString<ExtractedMap>(jsonContent)
            extractedMap.courses.forEach { newCourse(it, extractedMap.semesterPdf) }
            call.respond(mapOf("extracted_courses" to extractedMap.courses))

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
 * Get Semesters from the database.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getSemesters() {
    get("/course/semester") { call.respond(mapOf("semester_table" to dao.allSemesters())) }
}

/**
 * Get credits from the database.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getCredits() {
    get("/course/credit") { call.respond(mapOf("credit_table" to dao.allCredits())) }
}

/**
 * Route to filter courses in the database.
 *
 * @receiver The Route on which to define the route.
 */
fun Route.getFilteredCourses() {
    post("/course/filter") {
        logger.info("Received a request to filter courses")
        try {
            val filter: CourseFilter = call.receive<CourseFilter>()
            logger.info("Filter received: $filter")
            val filteredCourses = dao.filteredCourses(filter)
            logger.info("Filtered courses: $filteredCourses")
            call.respond(mapOf("course_table" to filteredCourses))
            logger.info("Response sent successfully")
        } catch (e: Exception) {
            logger.error("Error while filtering courses: ${e.message}")
        }
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
 * Calculates the course level from the CICS ID.
 *
 * @return The course level.
 */
private fun String.calculateCourseLevel(): Int =
    this.firstOrNull { it.isDigit() }?.digitToInt()?.times(ONE_HUNDRED) ?: 0

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
        val process =
            ProcessBuilder("python3", "extractor.py", filePath, "data/$outputId.json").start()
        val scriptOutput = process.inputStream.bufferedReader().use { it.readText() }
        val scriptError = process.errorStream.bufferedReader().use { it.readText() }
        logger.info("Script output: $scriptOutput")
        logger.error("Script error: $scriptError")
        outputId
    } catch (e: Exception) {
        "Error running script: ${e.message}"
    }

/**
 * Adds a course to the database.
 *
 * @param extractedCourse The course to add.
 * @param semester The semester the course was offered in.
 * @return True if the course was added successfully, False otherwise.
 */
private suspend fun newCourse(
    extractedCourse: ExtractedCourse,
    semester: Semester,
) {
    val instructors = mapProfessors(extractedCourse.instructors)

    val newInstructors = instructors.map { instructor -> updateProfessorIfNeeded(instructor) }

    val oldCourse = dao.courseLookup(extractedCourse.cicsId)
    logger.info("Finding old course: $oldCourse")
    val newCourse =
        Course(
            cicsId = extractedCourse.cicsId,
            courseLevel = extractedCourse.cicsId.calculateCourseLevel(),
            department = extractedCourse.department,
            name = extractedCourse.name,
            description = extractedCourse.description,
            undergrad = extractedCourse.cicsId.calculateCourseLevel() < GRAD_CUTOFF,
            credits = extractedCourse.credits,
            instructors = newInstructors,
            prerequisites = extractedCourse.prerequisites,
            semestersOffered = determineSemestersOffered(oldCourse, semester),
        )

    logger.info("Adding new course: $newCourse")
    if (oldCourse.isEmpty()) {
        dao.addNewCourse(newCourse)
    } else {
        dao.editCourse(newCourse, oldCourse[0].id!!)
    }
}

/**
 * Updates a professor in the database if they don't already exist.
 *
 * @param instructor The professor to update.
 */
private suspend fun updateProfessorIfNeeded(instructor: Professor): Professor {
    val existingProfessor = dao.professorLookup(instructor)
    if (existingProfessor.isEmpty()) {
        return dao.addNewProfessor(instructor)!!
    } else {
        logger.info("Professor already exists: ${instructor.firstName} ${instructor.lastName}")
        return existingProfessor[0]
    }
}

/**
 * Determines the semesters offered for a course.
 *
 * @param oldCourse The course to check.
 * @param semester The semester the course was offered in.
 * @return A list of semesters the course is offered in.
 */
private fun determineSemestersOffered(
    oldCourse: List<Course>,
    semester: Semester,
): List<Semester> {
    if (oldCourse.isEmpty()) {
        return listOf(semester)
    }

    val semestersOffered = oldCourse[0].semestersOffered
    return if (semestersOffered.any { it == semester }) {
        semestersOffered
    } else {
        semestersOffered + semester
    }
}

/**
 * Maps a list of professor strings to a list of Professor objects.
 *
 * @param profStrings The list of professor strings to map.
 * @return A list of Professor objects.
 */
private fun mapProfessors(profStrings: List<String>): List<Professor> =
    profStrings
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .filter { it.size == 2 }
        .map {
            logger.info("Mapping professor string to object: ${it.joinToString(" ")}")
            Professor(firstName = it[0], lastName = it[1])
        }
