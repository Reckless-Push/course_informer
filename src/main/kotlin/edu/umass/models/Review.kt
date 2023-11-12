package edu.umass.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * Represents a review of a course by a student.
 *
 * @property id The unique identifier for the review.
 * @property professor The professor who taught the course.
 * @property course The course being reviewed.
 * @property user The user who wrote the review.
 * @property date The date the review was written.
 * @property difficulty The difficulty rating given by the user.
 * @property quality The quality rating given by the user.
 * @property tags A list of tags given by the user.
 * @property comment The comment written by the user.
 * @property fromRmp Whether the review was imported from RateMyProfessor.
 */
@Serializable
data class Review(
    val id: Int? = null,
    val professor: Professor,
    val course: Course,
    val user: User,
    val date: LocalDateTime,
    val difficulty: Int,
    val quality: Int,
    val tags: List<String>?,
    val comment: String,
    val fromRmp: Boolean,
)
