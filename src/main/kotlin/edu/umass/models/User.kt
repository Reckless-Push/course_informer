package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * Represents a user with their details and associated information.
 *
 * @property id The unique identifier for the user.
 * @property firstName The user's first name.
 * @property lastName The user's last name.
 * @property email The user's email address.
 * @property favoriteCourses A list of courses the user has favorited.
 * @property reviews A list of reviews the user has written.
 */
@Serializable
data class User(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val favoriteCourses: List<Course> = emptyList(),
    val reviews: List<Review> = emptyList(),
)
