package edu.umass.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * Represents a review of a course by a student.
 *
 * @property id The unique identifier for the review.
 * @property professor The professor who taught the course.
 * @property course The course being reviewed.
 * @property userId The id of the user who wrote the review.
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
    val userId: Int,
    val date: LocalDateTime,
    val difficulty: Int,
    val quality: Int,
    val tags: List<String> = emptyList(),
    val comment: String,
    val fromRmp: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Review) {
            return true
        }

        return id == other.id &&
                professor == other.professor &&
                course == other.course &&
                userId == other.userId &&
                date == other.date &&
                difficulty == other.difficulty &&
                quality == other.quality &&
                tags == other.tags &&
                comment == other.comment &&
                fromRmp == other.fromRmp
    }
}
