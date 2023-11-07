package edu.umass.dao

import edu.umass.dao.DatabaseFactory.dbQuery
import edu.umass.models.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class DAOFacadeImpl : DAOFacade {
    private suspend fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        email = row[Users.email],
        favoriteCourses = row[Users.favoriteCourses]?.split(",")?.mapNotNull { course(it.toInt()) },
        reviews = row[Users.reviews]?.split(",")?.mapNotNull { review(it.toInt()) }
    )

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

    private fun resultRowToProfessor(row: ResultRow): Professor = Professor(
        id = row[Professors.id],
        firstName = row[Professors.firstName],
        lastName = row[Professors.lastName],
    )

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

    private fun setProfessorValues(
        it: UpdateBuilder<*>,
        firstName: String,
        lastName: String
    ) {
        it[Professors.firstName] = firstName
        it[Professors.lastName] = lastName
    }

    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map { resultRowToUser(it) }
    }

    override suspend fun user(id: Int): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map { resultRowToUser(it) }
            .singleOrNull()
    }

    override suspend fun addNewUser(firstName: String, lastName: String, email: String): User? = dbQuery {
        Users.insert {
            setUserValues(it, firstName, lastName, email)
        } get Users.id
    }.let { user(it) }

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

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    override suspend fun allCourses(): List<Course> = dbQuery {
        Courses.selectAll().map { resultRowToCourse(it) }
    }

    override suspend fun course(id: Int): Course? = dbQuery {
        Courses
            .select { Courses.cicsId eq id }
            .map { resultRowToCourse(it) }
            .singleOrNull()
    }

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

    override suspend fun deleteCourse(id: Int): Boolean = dbQuery {
        Courses.deleteWhere { cicsId eq id } > 0
    }

    override suspend fun allProfessors(): List<Professor> = dbQuery {
        Professors.selectAll().map { resultRowToProfessor(it) }
    }

    override suspend fun professor(id: Int): Professor? = dbQuery {
        Professors
            .select { Professors.id eq id }
            .map { resultRowToProfessor(it) }
            .singleOrNull()
    }

    override suspend fun addNewProfessor(firstName: String, lastName: String): Professor? = dbQuery {
        Professors.insert {
            setProfessorValues(it, firstName, lastName)
        } get Professors.id
    }.let { professor(it) }

    override suspend fun editProfessor(id: Int, firstName: String, lastName: String): Boolean = dbQuery {
        Professors.update({ Professors.id eq id }) {
            setProfessorValues(it, firstName, lastName)
        }
    } > 0

    override suspend fun deleteProfessor(id: Int): Boolean = dbQuery {
        Professors.deleteWhere { Professors.id eq id } > 0
    }

    override suspend fun allReviews(): List<Review> = dbQuery {
        Reviews.selectAll().map { resultRowToReview(it) }
    }

    override suspend fun review(id: Int): Review? = dbQuery {
        Reviews
            .select { Reviews.id eq id }
            .map { resultRowToReview(it) }
            .singleOrNull()
    }

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

    override suspend fun deleteReview(id: Int): Boolean = dbQuery {
        Reviews.deleteWhere { Reviews.id eq id } > 0
    }
}

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

