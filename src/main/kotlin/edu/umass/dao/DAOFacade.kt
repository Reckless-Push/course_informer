package edu.umass.dao

import edu.umass.models.Course
import edu.umass.models.Professor
import edu.umass.models.Review
import edu.umass.models.User

/**
 * DAOFacade provides an interface for data access operations related to university entities such as users, courses, professors, and reviews.
 */
interface DAOFacade {
    /**
     * Retrieves a list of all users.
     *
     * @return A list of User objects.
     */
    suspend fun allUsers(): List<User>

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier for a User.
     * @return A User object or null if not found.
     */
    suspend fun user(id: Int): User?

    /**
     * Adds a new user to the data store.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @return The newly created User object or null if the operation fails.
     */
    suspend fun addNewUser(
        firstName: String,
        lastName: String,
        email: String,
    ): User?

    /**
     * Edits an existing user's information.
     *
     * @param id The ID of the user to edit.
     * @param firstName The new first name for the user.
     * @param lastName The new last name for the user.
     * @param email The new email for the user.
     * @param favoriteCourses A list of the user's favorite courses, nullable.
     * @param reviews A list of the user's reviews, nullable.
     * @return True if the update was successful, False otherwise.
     */
    suspend fun editUser(
        id: Int,
        firstName: String,
        lastName: String,
        email: String,
        favoriteCourses: List<Course>?,
        reviews: List<Review>?,
    ): Boolean

    /**
     * Deletes a user from the data store.
     *
     * @param id The ID of the user to delete.
     * @return True if the deletion was successful, False otherwise.
     */
    suspend fun deleteUser(id: Int): Boolean

    /**
     * Retrieves a list of all courses.
     *
     * @return A list of Course objects.
     */
    suspend fun allCourses(): List<Course>

    /**
     * Retrieves a course by its unique identifier.
     *
     * @param id The unique identifier for a Course.
     * @return A Course object or null if not found.
     */
    suspend fun course(id: Int): Course?

    /**
     * Adds a new course to the data store.
     *
     * @param cicsId The CICS ID of the course.
     * @param name The name of the course.
     * @param description The description of the course.
     * @param credits The number of credits the course is worth.
     * @param undergraduateRequirements A list of undergraduate courses that are prerequisites for this course, nullable.
     * @param graduateRequirements A list of graduate courses that are prerequisites for this course, nullable.
     * @param semestersOffered A list of semesters that this course is offered, nullable.
     * @param courseLevel The level of the course (100, 200, 300, 400, 500, 600, 700, 800, 900).
     * @param professors A list of professors that teach this course, nullable.
     * @return The newly created Course object or null if the operation fails.
     */
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

    /**
     * Edits an existing course's information.
     *
     * @param id The ID of the course to edit.
     * @param cicsId The new CICS ID for the course.
     * @param name The new name for the course.
     * @param description The new description for the course.
     * @param credits The new number of credits for the course.
     * @param undergraduateRequirements A list of undergraduate courses that are prerequisites for this course, nullable.
     * @param graduateRequirements A list of graduate courses that are prerequisites for this course, nullable.
     * @param semestersOffered A list of semesters that this course is offered, nullable.
     * @param courseLevel The new level of the course (100, 200, 300, 400, 500, 600, 700, 800, 900).
     * @param professors A list of professors that teach this course, nullable.
     * @return True if the update was successful, False otherwise.
     */
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

    /**
     * Deletes a course from the data store.
     *
     * @param id The ID of the course to delete.
     * @return True if the deletion was successful, False otherwise.
     */
    suspend fun deleteCourse(id: Int): Boolean

    /**
     * Retrieves a list of all professors.
     *
     * @return A list of Professor objects.
     */
    suspend fun allProfessors(): List<Professor>

    /**
     * Retrieves a professor by their unique identifier.
     *
     * @param id The unique identifier for a Professor.
     * @return A Professor object or null if not found.
     */
    suspend fun professor(id: Int): Professor?

    /**
     * Adds a new professor to the data store.
     *
     * @param firstName The first name of the professor.
     * @param lastName The last name of the professor.
     * @return The newly created Professor object or null if the operation fails.
     */
    suspend fun addNewProfessor(
        firstName: String,
        lastName: String,
    ): Professor?

    /**
     * Edits an existing professor's information.
     *
     * @param id The ID of the professor to edit.
     * @param firstName The new first name for the professor.
     * @param lastName The new last name for the professor.
     * @return True if the update was successful, False otherwise.
     */
    suspend fun editProfessor(
        id: Int,
        firstName: String,
        lastName: String
    ): Boolean

    /**
     * Deletes a professor from the data store.
     *
     * @param id The ID of the professor to delete.
     * @return True if the deletion was successful, False otherwise.
     */
    suspend fun deleteProfessor(id: Int): Boolean

    /**
     * Retrieves a list of all reviews.
     *
     * @return A list of Review objects.
     */
    suspend fun allReviews(): List<Review>

    /**
     * Retrieves a review by its unique identifier.
     *
     * @param id The unique identifier for a Review.
     * @return A Review object or null if not found.
     */
    suspend fun review(id: Int): Review?

    /**
     * Adds a new review to the data store.
     *
     * @param professor The professor that the review is for.
     * @param course The course that the review is for.
     * @param user The user that created the review.
     * @param difficulty The difficulty rating for the course.
     * @param quality The quality rating for the course.
     * @param tags A list of tags for the review, nullable.
     * @param comment The comment for the review.
     * @param fromRMP Whether the review was imported from RateMyProfessor.
     * @return The newly created Review object or null if the operation fails.
     */
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

    /**
     * Edits an existing review's information.
     *
     * @param id The ID of the review to edit.
     * @param professor The new professor for the review.
     * @param course The new course for the review.
     * @param user The new user for the review.
     * @param difficulty The new difficulty rating for the course.
     * @param quality The new quality rating for the course.
     * @param tags The new list of tags for the review, nullable.
     * @param comment The new comment for the review.
     * @param fromRMP Whether the review was imported from RateMyProfessor.
     * @return True if the update was successful, False otherwise.
     */
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

    /**
     * Deletes a review from the data store.
     *
     * @param id The ID of the review to delete.
     * @return True if the deletion was successful, False otherwise.
     */
    suspend fun deleteReview(id: Int): Boolean
}