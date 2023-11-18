/**
 * Configures the user authentication for the Ktor application.
 *
 * @file Authentication.kt
 * @version 0.1
 */

package edu.umass.plugins

//  import edu.umass.routes.addCourse
//  import edu.umass.routes.addProfessor
//  import edu.umass.routes.addReview
//  import edu.umass.routes.addUser
//  import edu.umass.routes.deleteCourse
//  import edu.umass.routes.deleteProfessor
//  import edu.umass.routes.deleteReview
//  import edu.umass.routes.deleteUser
//  import edu.umass.routes.frontend
//  import edu.umass.routes.getCourse
//  import edu.umass.routes.getProfessor
//  import edu.umass.routes.getReview
//  import edu.umass.routes.getUser
//  import edu.umass.routes.healthCheck
//  import edu.umass.routes.listCourses
//  import edu.umass.routes.listProfessors
//  import edu.umass.routes.listReviews
//  import edu.umass.routes.listUsers
//  import edu.umass.routes.updateCourse
//  import edu.umass.routes.updateProfessor
//  import edu.umass.routes.updateReview
//  import edu.umass.routes.updateUser
//  import io.ktor.http.HttpStatusCode
//  import io.ktor.server.application.Application
//  import io.ktor.server.application.install
//  import io.ktor.server.plugins.statuspages.StatusPages
//  import io.ktor.server.response.respondText
//  import io.ktor.server.routing.routing
import io.ktor.server.application.HttpClient
import io.ktor.server.application.HttpMethod
import io.ktor.server.auth.OAuth2ServerSettings
import io.ktor.server.sessions.UserSession


fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("user_session")
    }
}

fun Application.configureAuthentication(httpClient: HttpClient) {
    val redirects = mutableMapOf<String, String>()
    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                        redirects[state] = call.request.queryParameters["redirectUrl"]!!
                    },
                )
            }
            client = httpClient
        }
    }
}
