package edu.umass.dao

import edu.umass.dao.DatabaseFactory.dbQuery
import edu.umass.models.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * Implementation of the DAOFacade interface providing concrete data access operations to interact with the database using Exposed as the ORM framework.
 */
class DAOFacadeImpl : DAOFacade {

    /**
     * Converts a database result row into a User object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A User domain model object.
     */
    private suspend fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        email = row[Users.email],
        favoriteCourses = row[Users.favoriteCourses]?.split(",")?.mapNotNull { course(it.toInt()) },
        reviews = row[Users.reviews]?.split(",")?.mapNotNull { review(it.toInt()) }
    )

    /**
     * Converts a database result row into a Review object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A Review domain model object.
     * @throws IllegalArgumentException If referenced professor, course, or user is not found.
     */
    private suspend fun resultRowToReview(row: ResultRow) = Review(
        id = row[Reviews.id],
        professor = professor(row[Reviews.professorId]) ?: throw IllegalArgumentException("Professor not found"),
        course = course(row[Reviews.courseId]) ?: throw IllegalArgumentException("Course not found"),
        user = user(row[Reviews.userId]) ?: throw IllegalArgumentException("User not found"),
        date = row[Reviews.datetime].toKotlinLocalDateTime(),
        difficulty = row[Reviews.difficulty],
        quality = row[Reviews.quality],
        tags = row[Reviews.tags].split(","),
        comment = row[Reviews.comment],
        fromRMP = row[Reviews.fromRMP]
    )

    /**
     * Converts a database result row into a Course object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A Course domain model object.
     */
    private suspend fun resultRowToCourse(row: ResultRow) = Course(
        cicsId = row[Courses.cicsId],
        name = row[Courses.name],
        description = row[Courses.description],
        credits = row[Courses.credits],
        undergraduateRequirements = row[Courses.undergraduateRequirements]?.split(",")
            ?.mapNotNull { course(it.toInt()) },
        graduateRequirements = row[Courses.graduateRequirements]?.split(",")?.mapNotNull { course(it.toInt()) },
        semestersOffered = row[Courses.semestersOffered]?.split(","),
        courseLevel = row[Courses.courseLevel],
        professors = row[Courses.professors]?.split(",")?.mapNotNull { professor(it.toInt()) }
    )

    /**
     * Converts a database result row into a Professor object.
     *
     * @param row The ResultRow object representing a database row.
     * @return A Professor domain model object.
     */
    private fun resultRowToProfessor(row: ResultRow): Professor = Professor(
        id = row[Professors.id],
        firstName = row[Professors.firstName],
        lastName = row[Professors.lastName],
    )

    /**
     * Sets the values for a User entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @param favoriteCourses Comma-separated string of favorite course IDs, nullable.
     * @param reviews Comma-separated string of review IDs, nullable.
     */
    private fun setUserValues(
        it: UpdateBuilder<*>,
        firstName: String,
        lastName: String,
        email: String,
        favoriteCourses: String? = null,
        reviews: String? = null
    ) {
        it[Users.firstName] = firstName
        it[Users.lastName] = lastName
        it[Users.email] = email
        it[Users.favoriteCourses] = favoriteCourses
        it[Users.reviews] = reviews
    }

    /**
     * Sets the values for a Review entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param professorId The ID of the professor being reviewed.
     * @param courseId The ID of the course being reviewed.
     * @param userId The ID of the user who wrote the review.
     * @param difficulty The difficulty rating of the course.
     * @param quality The quality rating of the course.
     * @param tags Comma-separated string of tags, nullable.
     * @param comment The comment for the review.
     * @param fromRMP Whether the review was imported from RMP.
     */
    private fun setReviewValues(
        it: UpdateBuilder<*>,
        professorId: Int,
        courseId: Int,
        userId: Int,
        difficulty: Int,
        quality: Int,
        tags: String,
        comment: String,
        fromRMP: Boolean
    ) {
        it[Reviews.professorId] = professorId
        it[Reviews.courseId] = courseId
        it[Reviews.userId] = userId
        it[Reviews.difficulty] = difficulty
        it[Reviews.quality] = quality
        it[Reviews.tags] = tags
        it[Reviews.comment] = comment
        it[Reviews.fromRMP] = fromRMP
        it[Reviews.datetime] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    }

    /**
     * Sets the values for a Course entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param cicsId The CICS ID of the course.
     * @param name The name of the course.
     * @param description The description of the course.
     * @param credits The number of credits for the course.
     * @param undergraduateRequirements Comma-separated string of undergraduate requirement course IDs, nullable.
     * @param graduateRequirements Comma-separated string of graduate requirement course IDs, nullable.
     * @param semestersOffered Comma-separated string of semesters offered, nullable.
     * @param courseLevel The course level.
     * @param professors Comma-separated string of professor IDs, nullable.
     */
    private fun setCourseValues(
        it: UpdateBuilder<*>,
        cicsId: Int,
        name: String,
        description: String,
        credits: Int,
        undergraduateRequirements: String? = null,
        graduateRequirements: String? = null,
        semestersOffered: String? = null,
        courseLevel: Int,
        professors: String? = null
    ) {
        it[Courses.cicsId] = cicsId
        it[Courses.name] = name
        it[Courses.description] = description
        it[Courses.credits] = credits
        it[Courses.undergraduateRequirements] = undergraduateRequirements
        it[Courses.graduateRequirements] = graduateRequirements
        it[Courses.semestersOffered] = semestersOffered
        it[Courses.courseLevel] = courseLevel
        it[Courses.professors] = professors
    }

    /**
     * Sets the values for a Professor entity during an insert or update operation.
     *
     * @param it The UpdateBuilder instance used in insert/update operations.
     * @param firstName The first name of the professor.
     * @param lastName The last name of the professor.
     */
    private fun setProfessorValues(
        it: UpdateBuilder<*>,
        firstName: String,
        lastName: String
    ) {
        it[Professors.firstName] = firstName
        it[Professors.lastName] = lastName
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
        Users
            .select { Users.id eq id }
            .map { resultRowToUser(it) }
            .singleOrNull()
    }

    /**
     * Inserts a new user into the database.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @return The newly created User object or null if the operation fails.
     */
    override suspend fun addNewUser(firstName: String, lastName: String, email: String): User? = dbQuery {
        Users.insert {
            setUserValues(it, firstName, lastName, email)
        } get Users.id
    }.let { user(it) }

    /**
     * Updates an existing user's information in the database.
     *
     * @param id The ID of the user to update.
     * @param firstName The updated first name for the user.
     * @param lastName The updated last name for the user.
     * @param email The updated email for the user.
     * @param favoriteCourses An optional list of the user's favorite courses.
     * @param reviews An optional list of the user's reviews.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editUser(
        id: Int,
        firstName: String,
        lastName: String,
        email: String,
        favoriteCourses: List<Course>?,
        reviews: List<Review>?,
    ): Boolean {
        val updatedRows = dbQuery {
            Users.update({ Users.id eq id }) {
                setUserValues(
                    it, firstName, lastName, email,
                    favoriteCourses?.map(Course::cicsId)?.joinToString(","),
                    reviews?.map(Review::id)?.joinToString(",")
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
        Courses
            .select { Courses.cicsId eq id }
            .map { resultRowToCourse(it) }
            .singleOrNull()
    }

    /**
     * Inserts a new course into the database.
     *
     * @param cicsId The department-specific ID for the course.
     * @param name The name of the course.
     * @param description The description of the course.
     * @param credits The number of credits the course is worth.
     * @param undergraduateRequirements An optional list of prerequisite courses for undergraduates.
     * @param graduateRequirements An optional list of prerequisite courses for graduates.
     * @param semestersOffered An optional list of semesters in which the course is offered.
     * @param courseLevel The academic level of the course.
     * @param professors An optional list of professors teaching the course.
     * @return The newly created Course object or null if the operation fails.
     */
    override suspend fun addNewCourse(
        cicsId: Int,
        name: String,
        description: String,
        credits: Int,
        undergraduateRequirements: List<Course>?,
        graduateRequirements: List<Course>?,
        semestersOffered: List<String>?,
        courseLevel: Int,
        professors: List<Professor>?
    ): Course? = dbQuery {
        Courses.insert {
            setCourseValues(
                it, cicsId, name, description, credits,
                undergraduateRequirements?.map(Course::cicsId)?.joinToString(","),
                graduateRequirements?.map(Course::cicsId)?.joinToString(","),
                semestersOffered?.joinToString(","),
                courseLevel,
                professors?.map(Professor::id)?.joinToString(",")
            )
        } get Courses.cicsId
    }.let { course(it) }

    /**
     * Updates an existing course's information in the database.
     *
     * @param id The CICS ID of the course to update.
     * @param cicsId The updated department-specific ID for the course.
     * @param name The updated name of the course.
     * @param description The updated description of the course.
     * @param credits The updated number of credits the course is worth.
     * @param undergraduateRequirements The updated list of prerequisite courses for undergraduates.
     * @param graduateRequirements The updated list of prerequisite courses for graduates.
     * @param semestersOffered The updated list of semesters in which the course is offered.
     * @param courseLevel The updated academic level of the course.
     * @param professors The updated list of professors teaching the course.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editCourse(
        id: Int,
        cicsId: Int,
        name: String,
        description: String,
        credits: Int,
        undergraduateRequirements: List<Course>,
        graduateRequirements: List<Course>,
        semestersOffered: List<String>,
        courseLevel: Int,
        professors: List<Professor>
    ): Boolean = dbQuery {
        Courses.update({ Courses.cicsId eq id }) {
            setCourseValues(
                it, cicsId, name, description, credits,
                undergraduateRequirements.map(Course::cicsId).joinToString(","),
                graduateRequirements.map(Course::cicsId).joinToString(","),
                semestersOffered.joinToString(","),
                courseLevel,
                professors.map(Professor::id).joinToString(",")
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
        Professors
            .select { Professors.id eq id }
            .map { resultRowToProfessor(it) }
            .singleOrNull()
    }

    /**
     * Inserts a new professor into the database.
     *
     * @param firstName The first name of the professor.
     * @param lastName The last name of the professor.
     * @return The newly created Professor object or null if the operation fails.
     */
    override suspend fun addNewProfessor(firstName: String, lastName: String): Professor? = dbQuery {
        Professors.insert {
            setProfessorValues(it, firstName, lastName)
        } get Professors.id
    }.let { professor(it) }

    /**
     * Updates an existing professor's information in the database.
     *
     * @param id The ID of the professor to update.
     * @param firstName The updated first name of the professor.
     * @param lastName The updated last name of the professor.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editProfessor(id: Int, firstName: String, lastName: String): Boolean = dbQuery {
        Professors.update({ Professors.id eq id }) {
            setProfessorValues(it, firstName, lastName)
        }
    } > 0

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
        Reviews
            .select { Reviews.id eq id }
            .map { resultRowToReview(it) }
            .singleOrNull()
    }

    /**
     * Inserts a new review into the database.
     *
     * @param professor The professor being reviewed.
     * @param course The course being reviewed.
     * @param user The user who wrote the review.
     * @param difficulty The difficulty rating of the course.
     * @param quality The quality rating of the course.
     * @param tags An optional list of tags for the review.
     * @param comment The comment for the review.
     * @param fromRMP Whether the review was imported from RMP.
     * @return The newly created Review object or null if the operation fails.
     */
    override suspend fun addNewReview(
        professor: Professor,
        course: Course,
        user: User,
        difficulty: Int,
        quality: Int,
        tags: List<String>,
        comment: String,
        fromRMP: Boolean
    ): Review? = dbQuery {
        Reviews.insert {
            setReviewValues(
                it,
                professor.id,
                course.cicsId,
                user.id,
                difficulty,
                quality,
                tags.joinToString(","),
                comment,
                fromRMP
            )
        } get Reviews.id
    }.let { review(it) }

    /**
     * Updates an existing review's information in the database.
     *
     * @param id The ID of the review to update.
     * @param professor The new professor for the review.
     * @param course The new course for the review.
     * @param user The new user for the review.
     * @param difficulty The new difficulty rating for the course.
     * @param quality The new quality rating for the course.
     * @param tags The new list of tags for the review.
     * @param comment The new comment for the review.
     * @param fromRMP Whether the review was imported from RateMyProfessor.
     * @return True if the update was successful, False otherwise.
     */
    override suspend fun editReview(
        id: Int,
        professor: Professor,
        course: Course,
        user: User,
        difficulty: Int,
        quality: Int,
        tags: List<String>,
        comment: String,
        fromRMP: Boolean
    ): Boolean = dbQuery {
        Reviews.update({ Reviews.id eq id }) {
            setReviewValues(
                it,
                professor.id,
                course.cicsId,
                user.id,
                difficulty,
                quality,
                tags.joinToString(","),
                comment,
                fromRMP
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
 * Singleton instance of the DAOFacade interface.
 */
val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        // Initialize users, if not present
        if (allUsers().isEmpty()) {
            addNewUser("John", "Doe", "johndoe@example.com")
        }

        // Initialize courses, if not present
        if (allCourses().isEmpty()) {
            addNewCourse(
                101,
                "Intro to Programming",
                "An introductory course on programming",
                4,
                null,
                null,
                listOf("Fall", "Spring"),
                100,
                null
            )
        }

        // Initialize professors, if not present
        if (allProfessors().isEmpty()) {
            addNewProfessor("Jane", "Smith")
        }

        // Initialize reviews, if not present
        if (allReviews().isEmpty()) {
            addNewReview(
                Professor(1, "Jane", "Smith"),
                Course(
                    101,
                    "Intro to Programming",
                    "An introductory course on programming",
                    4,
                    null,
                    null,
                    listOf("Fall", "Spring"),
                    100,
                    listOf()
                ),
                User(1, "John", "Doe", "johndoe@example.com", null, null),
                5,
                5,
                listOf("challenging", "rewarding"),
                "Great course for beginners!",
                false
            )
        }
    }
}

