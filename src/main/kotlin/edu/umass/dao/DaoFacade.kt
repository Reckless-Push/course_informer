package edu.umass.dao

import edu.umass.models.Course
import edu.umass.models.Professor
import edu.umass.models.Review
import edu.umass.models.User

/**
 * DaoFacade provides an interface for data access operations related to university entities such as
 * users, courses, professors, and reviews.
 */
interface DaoFacade {
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
     * @param user The user to add.
     * @return The newly created User object or null if the operation fails.
     */
    suspend fun addNewUser(user: User): User?

    /**
     * Edits an existing user's information.
     *
     * @param user The updated user to replace in the database.
     * @param id The ID of the user to update.
     * @return True if the update was successful, False otherwise.
     */
    suspend fun editUser(
        user: User,
        id: Int,
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
     * @param course The course to add.
     * @return The newly created Course object or null if the operation fails.
     */
    suspend fun addNewCourse(course: Course): Course?

    /**
     * Edits an existing course's information.
     *
     * @param course The updated course to replace in the database.
     * @param cicsId The ID of the course to update.
     * @return True if the update was successful, False otherwise.
     */
    suspend fun editCourse(
        course: Course,
        cicsId: Int,
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
     * @param professor The professor to add.
     * @return The newly created Professor object or null if the operation fails.
     */
    suspend fun addNewProfessor(professor: Professor): Professor?

    /**
     * Edits an existing professor's information.
     *
     * @param professor The updated professor to replace in the database.
     * @param id The ID of the professor to update.
     * @return True if the update was successful, False otherwise.
     */
    suspend fun editProfessor(
        professor: Professor,
        id: Int,
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
     * @param review The review to add.
     * @return The newly created Review object or null if the operation fails.
     */
    suspend fun addNewReview(review: Review): Review?

    /**
     * Edits an existing review's information.
     *
     * @param review The updated review to replace in the database.
     * @param id The ID of the review to update.
     * @return True if the update was successful, False otherwise.
     */
    suspend fun editReview(
        review: Review,
        id: Int,
    ): Boolean

    /**
     * Deletes a review from the data store.
     *
     * @param id The ID of the review to delete.
     * @return True if the deletion was successful, False otherwise.
     */
    suspend fun deleteReview(id: Int): Boolean
}
