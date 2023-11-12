/**
 * Configures routing and error handling for the Ktor application.
 *
 * @file Routing.kt
 * @version 0.1
 */

package edu.umass.plugins

import edu.umass.routes.addCourse
import edu.umass.routes.addProfessor
import edu.umass.routes.addReview
import edu.umass.routes.addUser
import edu.umass.routes.deleteCourse
import edu.umass.routes.deleteProfessor
import edu.umass.routes.deleteReview
import edu.umass.routes.deleteUser
import edu.umass.routes.frontend
import edu.umass.routes.getCourse
import edu.umass.routes.getProfessor
import edu.umass.routes.getReview
import edu.umass.routes.getUser
import edu.umass.routes.healthCheck
import edu.umass.routes.listCourses
import edu.umass.routes.listProfessors
import edu.umass.routes.listReviews
import edu.umass.routes.listUsers
import edu.umass.routes.updateCourse
import edu.umass.routes.updateProfessor
import edu.umass.routes.updateReview
import edu.umass.routes.updateUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing

/**
 * Configures routing and error handling for the Ktor application.
 *
 * @receiver The Application on which to install plugins and configure routing.
 */
fun Application.configureRouting() {
    // Install the StatusPages feature to handle exceptions globally.
    install(StatusPages) {
        // Catch all exceptions, log them, and respond with a 500 Internal Server Error status.
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    // Define the routing for the application.
    routing {
        frontend()
        healthCheck()

        listReviews()
        addReview()
        getReview()
        updateReview()
        deleteReview()

        listProfessors()
        addProfessor()
        getProfessor()
        updateProfessor()
        deleteProfessor()

        listUsers()
        addUser()
        getUser()
        updateUser()
        deleteUser()

        listCourses()
        addCourse()
        getCourse()
        updateCourse()
        deleteCourse()
    }
}
