package edu.umass.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val favoriteCourses: List<Course>?,
    val reviews: List<Review>?,
)