package edu.umass.dao

import edu.umass.models.Course
import edu.umass.models.Professor
import edu.umass.models.Review
import edu.umass.models.User

interface DAOFacade {
    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun addNewUser(
        firstName: String,
        lastName: String,
        email: String,
    ): User?

    suspend fun editUser(
        id: Int,
        firstName: String,
        lastName: String,
        email: String,
        favoriteCourses: List<Course>?,
        reviews: List<Review>?,
    ): Boolean

    suspend fun deleteUser(id: Int): Boolean

    suspend fun allCourses(): List<Course>
    suspend fun course(id: Int): Course?
    suspend fun addNewCourse(
        cicsId: Int,
        name: String,
        description: String,
        credits: Int,
        undergraduateRequirements: List<Course>?,
        graduateRequirements: List<Course>?,
        semestersOffered: List<String>?,
        courseLevel: Int,
        professors: List<Professor>?
    ): Course?

    suspend fun editCourse(
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
    ): Boolean

    suspend fun deleteCourse(id: Int): Boolean

    suspend fun allProfessors(): List<Professor>
    suspend fun professor(id: Int): Professor?
    suspend fun addNewProfessor(
        firstName: String,
        lastName: String,
    ): Professor?

    suspend fun editProfessor(
        id: Int,
        firstName: String,
        lastName: String
    ): Boolean

    suspend fun deleteProfessor(id: Int): Boolean

    suspend fun allReviews(): List<Review>
    suspend fun review(id: Int): Review?
    suspend fun addNewReview(
        professor: Professor,
        course: Course,
        user: User,
        difficulty: Int,
        quality: Int,
        tags: List<String>,
        comment: String,
        fromRMP: Boolean
    ): Review?

    suspend fun editReview(
        id: Int,
        professor: Professor,
        course: Course,
        user: User,
        difficulty: Int,
        quality: Int,
        tags: List<String>,
        comment: String,
        fromRMP: Boolean
    ): Boolean

    suspend fun deleteReview(id: Int): Boolean
}