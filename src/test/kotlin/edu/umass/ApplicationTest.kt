package edu.umass

import edu.umass.dao.DatabaseFactory
import edu.umass.dao.dao
import edu.umass.models.Course
import edu.umass.models.Professor
import edu.umass.models.Review
import edu.umass.models.Semester
import edu.umass.models.SemesterSeason
import edu.umass.models.User
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.server.testing.testApplication
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class ApplicationTest {
    // Dummy Semester objects
    val fallSemester = Semester(SemesterSeason.FALL, 2024)
    val springSemester = Semester(SemesterSeason.SPRING, 2024)

    // Dummy Professor objects
    val professor1 = Professor(10, "Vio", "Lin")
    val professor2 = Professor(11, "Guy", "Tar")
    val professor3 = Professor(12, "Sam", "Pler")

    // Dummy Course objects
    val undergradCourse1 =
        Course(
            cicsId = 123,
            name = "Introduction to Programming",
            description = "Fundamentals of programming concepts",
            credits = 3,
            courseLevel = 100,
            semestersOffered = listOf(fallSemester),
            professors = listOf(professor1),
        )
    val undergradCourse2 =
        Course(
            cicsId = 234,
            name = "Linear Algebra",
            description = "Basic concepts of linear algebra",
            credits = 4,
            courseLevel = 200,
            semestersOffered = listOf(springSemester),
            professors = listOf(professor2),
        )
    val gradCourse =
        Course(
            cicsId = 678,
            name = "Advanced Debugging",
            description = "In-depth study of advanced debugging",
            credits = 3,
            courseLevel = 600,
            undergraduateRequirements = listOf(undergradCourse1, undergradCourse2),
            semestersOffered = listOf(fallSemester),
            professors = listOf(professor3),
        )

    // Dummy Review objects
    val review1 =
        Review(
            id = 1234,
            professor = professor1,
            course = undergradCourse1,
            userId = 10,
            date = LocalDateTime(2023, 10, 12, 18, 5),
            difficulty = 3,
            quality = 4,
            tags = listOf("helpful", "tough grader"),
            comment = "A lot of people in this class.",
            fromRmp = false,
        )

    // Dummy User objects
    val user1 =
        User(
            id = 10,
            firstName = "Lego",
            lastName = "Bricks",
            email = "lego.bricks@gmail.com",
            favoriteCourses = listOf(undergradCourse1, undergradCourse2),
            reviews = listOf(review1),
        )

    @BeforeTest
    fun setup() {
        DatabaseFactory.init()
    }

    @Test
    fun testRoot() = testApplication {
        val response = client.get("/") { url { protocol = URLProtocol.HTTPS } }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testAddProfessor() = testApplication {
        withContext(Dispatchers.Default) {
            val response: Professor? = dao.addNewProfessor(professor1
            assertTrue(professor1.equals(response))
        }
    }

    @Test
    fun testAddUser() = testApplication {
        withContext(Dispatchers.Default) {
            val response: User? = dao.addNewUser(user1)
            assertTrue(user1.equals(response))
        }
    }

    @Test
    fun testAddReview() = testApplication {
        withContext(Dispatchers.Default) {
            val response: Review? = dao.addNewReview(review1)
            assertTrue(review1.equals(response))
        }
    }

    @Test
    fun testAddCourse() = testApplication {
        withContext(Dispatchers.Default) {
            val response: Course? = dao.addNewCourse(gradCourse)
            assertTrue(gradCourse.equals(response))
        }
    }
}
