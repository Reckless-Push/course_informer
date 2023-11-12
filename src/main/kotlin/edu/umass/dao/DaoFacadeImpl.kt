package edu.umass.dao

import edu.umass.dao.DatabaseFactory.dbQuery
import edu.umass.models.Course
import edu.umass.models.Courses
import edu.umass.models.Professor
import edu.umass.models.Professors
import edu.umass.models.Review
import edu.umass.models.Reviews
import edu.umass.models.Semester
import edu.umass.models.SemesterSeason
import edu.umass.models.User
import edu.umass.models.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime

const val DEFAULT_CICS_ID = 101
const val DEFAULT_CREDITS = 4
const val DEFAULT_COURSE_LEVEL = 100
const val DEFAULT_YEAR = 2021
const val DEFAULT_DIFFICULTY = 5
const val DEFAULT_QUALITY = 5

/** The singleton instance of the DaoFacade. */
val dao: DaoFacade =
    DaoFacadeImpl().apply {
        runBlocking {
            initializeUsers(this@apply)
            initializeCourses(this@apply)
            initializeProfessors(this@apply)
            initializeReviews(this@apply)
        }
    }

/**
 * Implementation of the DAOFacade interface providing concrete data access operations to interact
 * with the database using Exposed as the ORM framework.
 */
class DaoFacadeImpl : DaoFacade {
    /**
     * Converts a database result row into a User object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A User domain model object.
     */
    private suspend fun resultRowToUser(row: ResultRow) =
        User(
            id = row[Users.id],
            firstName = row[Users.firstName],
            lastName = row[Users.lastName],
            email = row[Users.email],
            favoriteCourses =
                row[Users.favoriteCourses]?.split(",")?.mapNotNull { course(it.toInt()) },
            reviews = row[Users.reviews]?.split(",")?.mapNotNull { review(it.toInt()) },
        )

    /**
     * Converts a database result row into a Review object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A Review domain model object.
     * @throws IllegalArgumentException If referenced professor, course, or user is not found.
     */
    private suspend fun resultRowToReview(row: ResultRow) =
        Review(
            id = row[Reviews.id],
            professor = professor(row[Reviews.professorId])
                ?: throw IllegalArgumentException("Professor not found"),
            course = course(row[Reviews.courseId])
                ?: throw IllegalArgumentException("Course not found"),
            user = user(row[Reviews.userId]) ?: throw IllegalArgumentException("User not found"),
            date = row[Reviews.datetime].toKotlinLocalDateTime(),
            difficulty = row[Reviews.difficulty],
            quality = row[Reviews.quality],
            tags = row[Reviews.tags]?.split(","),
            comment = row[Reviews.comment],
            fromRmp = row[Reviews.fromRmp],
        )

    /**
     * Converts a database result row into a Course object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A Course domain model object.
     */
    private suspend fun resultRowToCourse(row: ResultRow) =
        Course(
            cicsId = row[Courses.cicsId],
            name = row[Courses.name],
            description = row[Courses.description],
            credits = row[Courses.credits],
            undergraduateRequirements =
                row[Courses.undergraduateRequirements]?.split(",")?.mapNotNull { course(it.toInt()) },
            graduateRequirements =
                row[Courses.graduateRequirements]?.split(",")?.mapNotNull { course(it.toInt()) },
            semestersOffered =
                row[Courses.semestersOffered]?.split(",")?.mapNotNull { parseSemester(it) },
            courseLevel = row[Courses.courseLevel],
            professors = row[Courses.professors]?.split(",")?.mapNotNull { professor(it.toInt()) },
        )

    /**
     * Converts a database result row into a Professor object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A Professor domain model object.
     */
    private fun resultRowToProfessor(row: ResultRow): Professor =
        Professor(
            id = row[Professors.id],
            firstName = row[Professors.firstName],
            lastName = row[Professors.lastName],
        )

    /**
     * Converts a string into a Semester object.
     *
     * @param semesterStr The string to be parsed.
     * @return A Semester object or null if the string is not in the correct format.
     */
    private fun parseSemester(semesterStr: String): Semester? {
        val (seasonStr, yearStr) = semesterStr.trim().split(" ").takeIf { it.size == 2 } ?: return null
        val season = SemesterSeason.entries.find { it.season == seasonStr } ?: return null
        val year = yearStr.toIntOrNull() ?: return null
        return Semester(season, year)
    }

    /**
     * Sets the values for a User entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param user The user to set.
     */
    private fun setUserValues(
        it: UpdateBuilder<*>,
        user: User,
    ) {
        it[Users.firstName] = user.firstName
        it[Users.lastName] = user.lastName
        it[Users.email] = user.email
        it[Users.favoriteCourses] = user.favoriteCourses?.map(Course::cicsId)?.joinToString(",")
        it[Users.reviews] = user.reviews?.map(Review::id)?.joinToString(",")
    }

    /**
     * Sets the values for a Review entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param review The review to set.
     */
    private fun setReviewValues(
        it: UpdateBuilder<*>,
        review: Review,
    ) {
        it[Reviews.professorId] =
                review.professor.id ?: throw IllegalArgumentException("Professor ID is null")
        it[Reviews.courseId] =
                review.course.cicsId ?: throw IllegalArgumentException("Course ID is null")
        it[Reviews.userId] = review.user.id ?: throw IllegalArgumentException("User ID is null")
        it[Reviews.difficulty] = review.difficulty
        it[Reviews.quality] = review.quality
        it[Reviews.tags] = review.tags?.joinToString(",")
        it[Reviews.comment] = review.comment
        it[Reviews.fromRmp] = review.fromRmp
        it[Reviews.datetime] = review.date.toJavaLocalDateTime()
    }

    /**
     * Sets the values for a Course entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param course The course to set.
     */
    private fun setCourseValues(
        it: UpdateBuilder<*>,
        course: Course,
    ) {
        it[Courses.cicsId] = course.cicsId ?: throw IllegalArgumentException("CICS ID is null")
        it[Courses.name] = course.name
        it[Courses.description] = course.description
        it[Courses.credits] = course.credits
        it[Courses.undergraduateRequirements] =
                course.undergraduateRequirements?.map(Course::cicsId)?.joinToString(",")
        it[Courses.graduateRequirements] =
                course.graduateRequirements?.map(Course::cicsId)?.joinToString(",")
        it[Courses.semestersOffered] = course.semestersOffered?.joinToString(",")
        it[Courses.courseLevel] = course.courseLevel
        it[Courses.professors] = course.professors?.map(Professor::id)?.joinToString(",")
    }

    /**
     * Sets the values for a Professor entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param professor The professor to set.
     */
    private fun setProfessorValues(
        it: UpdateBuilder<*>,
        professor: Professor,
    ) {
        it[Professors.firstName] = professor.firstName
        it[Professors.lastName] = professor.lastName
    }

    /**
     * Retrieves a list of all users in the database.
     *
     * @return A list of User objects.
     */
    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map { resultRowToUser(it) }
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier for a User.
     * @return A User object or null if not found.
     */
    override suspend fun user(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }.map { resultRowToUser(it) }.singleOrNull()
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user The user to add.
     * @return The newly created User object or null if the operation fails.
     */
    override suspend fun addNewUser(user: User): User? =
        user(dbQuery { Users.insert { setUserValues(it, user) } get Users.id })

    /**
     * Updates an existing user's information in the database.
     *
     * @param user The updated user to replace in the database.
     * @param id The ID of the user to update.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editUser(
        user: User,
        id: Int,
    ): Boolean {
        val updatedRows = dbQuery {
            Users.update({ Users.id eq id }) {
                setUserValues(
                    it,
                    user,
                )
            }
        }

        return updatedRows > 0
    }

    /**
     * Deletes a user from the database.
     *
     * @param id The ID of the user to delete.
     * @return True if the user was successfully deleted, False otherwise.
     */
    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    /**
     * Retrieves a list of all courses in the database.
     *
     * @return A list of Course objects.
     */
    override suspend fun allCourses(): List<Course> = dbQuery {
        Courses.selectAll().map { resultRowToCourse(it) }
    }

    /**
     * Retrieves a course by its CICS ID.
     *
     * @param id The CICS ID of the course.
     * @return A Course object or null if the course is not found.
     */
    override suspend fun course(id: Int): Course? = dbQuery {
        Courses.select { Courses.cicsId eq id }.map { resultRowToCourse(it) }.singleOrNull()
    }

    /**
     * Inserts a new course into the database.
     *
     * @param course The course to add.
     * @return The newly created Course object or null if the operation fails.
     */
    override suspend fun addNewCourse(course: Course): Course? =
        course(dbQuery { Courses.insert { setCourseValues(it, course) } get Courses.cicsId })

    /**
     * Updates an existing course's information in the database.
     *
     * @param course The updated course to replace in the database.
     * @param cicsId The CICS ID of the course to update.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editCourse(
        course: Course,
        cicsId: Int,
    ): Boolean =
        dbQuery {
            Courses.update({ Courses.cicsId eq cicsId }) {
                setCourseValues(
                    it,
                    course,
                )
            }
        } > 0

    /**
     * Deletes a course from the database.
     *
     * @param id The CICS ID of the course to delete.
     * @return True if the course was successfully deleted, False otherwise.
     */
    override suspend fun deleteCourse(id: Int): Boolean = dbQuery {
        Courses.deleteWhere { cicsId eq id } > 0
    }

    /**
     * Retrieves a list of all professors from the database.
     *
     * @return A list of Professor objects.
     */
    override suspend fun allProfessors(): List<Professor> = dbQuery {
        Professors.selectAll().map { resultRowToProfessor(it) }
    }

    /**
     * Retrieves a professor by their unique ID.
     *
     * @param id The unique ID of the professor.
     * @return A Professor object or null if the professor is not found.
     */
    override suspend fun professor(id: Int): Professor? = dbQuery {
        Professors.select { Professors.id eq id }.map { resultRowToProfessor(it) }.singleOrNull()
    }

    /**
     * Inserts a new professor into the database.
     *
     * @param professor
     * @return The newly created Professor object or null if the operation fails.
     */
    override suspend fun addNewProfessor(professor: Professor): Professor? =
        professor(
            dbQuery { Professors.insert { setProfessorValues(it, professor) } get Professors.id },
        )

    /**
     * Updates an existing professor's information in the database.
     *
     * @param professor The updated professor to replace in the database.
     * @param id The ID of the professor to update.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editProfessor(
        professor: Professor,
        id: Int,
    ): Boolean =
        dbQuery { Professors.update({ Professors.id eq id }) { setProfessorValues(it, professor) } } >
                0

    /**
     * Deletes a professor from the database.
     *
     * @param id The ID of the professor to delete.
     * @return True if the professor was successfully deleted, False otherwise.
     */
    override suspend fun deleteProfessor(id: Int): Boolean = dbQuery {
        Professors.deleteWhere { Professors.id eq id } > 0
    }

    /**
     * Retrieves a list of all reviews from the database.
     *
     * @return A list of Review objects.
     */
    override suspend fun allReviews(): List<Review> = dbQuery {
        Reviews.selectAll().map { resultRowToReview(it) }
    }

    /**
     * Retrieves a review by its unique identifier.
     *
     * @param id The unique identifier for a Review.
     * @return A Review object or null if not found.
     */
    override suspend fun review(id: Int): Review? = dbQuery {
        Reviews.select { Reviews.id eq id }.map { resultRowToReview(it) }.singleOrNull()
    }

    /**
     * Inserts a new review into the database.
     *
     * @param review The review to add.
     * @return The newly created Review object or null if the operation fails.
     */
    override suspend fun addNewReview(review: Review): Review? =
        review(
            dbQuery { Reviews.insert { setReviewValues(it, review) } get Reviews.id },
        )

    /**
     * Updates an existing review's information in the database.
     *
     * @param review The updated review to replace in the database.
     * @param id The ID of the review to update.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editReview(
        review: Review,
        id: Int,
    ): Boolean =
        dbQuery {
            Reviews.update({ Reviews.id eq id }) {
                setReviewValues(
                    it,
                    review,
                )
            }
        } > 0

    /**
     * Deletes a review from the database.
     *
     * @param id The ID of the review to delete.
     * @return True if the review was successfully deleted, False otherwise.
     */
    override suspend fun deleteReview(id: Int): Boolean = dbQuery {
        Reviews.deleteWhere { Reviews.id eq id } > 0
    }
}

/**
 * Initializes the database with default user data if it is empty.
 *
 * @param daoFacade The DaoFacade instance to use for database operations.
 */
private suspend fun initializeUsers(daoFacade: DaoFacade) {
    if (daoFacade.allUsers().isEmpty()) {
        val user = User(1, "John", "Doe", "johndoe@example.com", null, null)
        daoFacade.addNewUser(user)
    }
}

/**
 * Initializes the database with default course data if it is empty.
 *
 * @param daoFacade The DaoFacade instance to use for database operations.
 */
private suspend fun initializeCourses(daoFacade: DaoFacade) {
    if (daoFacade.allCourses().isEmpty()) {
        val semester = Semester(SemesterSeason.SPRING, DEFAULT_YEAR)
        val course =
            Course(
                DEFAULT_CICS_ID,
                "Intro to Programming",
                "An introductory course on programming",
                DEFAULT_CREDITS,
                null,
                null,
                listOf(semester.copy()),
                DEFAULT_COURSE_LEVEL,
                null,
            )
        daoFacade.addNewCourse(course)
    }
}

/**
 * Initializes the database with default professor data if it is empty.
 *
 * @param daoFacade The DaoFacade instance to use for database operations.
 */
private suspend fun initializeProfessors(daoFacade: DaoFacade) {
    if (daoFacade.allProfessors().isEmpty()) {
        val professor = Professor(1, "Jane", "Smith")
        daoFacade.addNewProfessor(professor)
    }
}

/**
 * Initializes the database with default review data if it is empty.
 *
 * @param daoFacade The DaoFacade instance to use for database operations.
 */
private suspend fun initializeReviews(daoFacade: DaoFacade) {
    if (daoFacade.allReviews().isEmpty()) {
        val review = createDefaultReview()
        daoFacade.addNewReview(review)
    }
}

/**
 * Creates a default review for testing purposes.
 *
 * @return A Review object.
 */
private fun createDefaultReview(): Review {
    val professor = Professor(1, "Jane", "Smith")
    val course =
        Course(
            DEFAULT_CICS_ID,
            "Intro to Programming",
            "An introductory course on programming",
            DEFAULT_CREDITS,
            null,
            null,
            listOf(Semester(SemesterSeason.SPRING, DEFAULT_YEAR).copy()),
            DEFAULT_COURSE_LEVEL,
            null,
        )
    val user = User(1, "John", "Doe", "johndoe@example.com", null, null)

    return Review(
        1,
        professor.copy(),
        course.copy(),
        user.copy(),
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        DEFAULT_DIFFICULTY,
        DEFAULT_QUALITY,
        listOf("challenging", "rewarding"),
        "Great course for beginners!",
        false,
    )
}
