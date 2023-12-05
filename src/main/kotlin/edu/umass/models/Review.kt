package edu.umass.models

import java.util.UUID
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
 * @property comment The comment written by the user.
 * @property fromRmp Whether the review was imported from RateMyProfessor.
 * @property forCredit Whether the review was for credit.
 * @property attendance Whether the review required attendance.
 * @property textbook Whether the review required a textbook.
 * @property grade The grade the user received in the course.
 */
@Serializable
data class Review(
    val id: Int? = null,
    val professor: Professor,
    val course: Course,
    @Serializable(with = UuidSerializer::class) val userId: UUID?,
    val date: LocalDateTime?,
    val difficulty: Int,
    val quality: Int,
    val comment: String,
    val fromRmp: Boolean,
    val forCredit: Boolean,
    val attendance: Boolean,
    val textbook: Boolean,
    @Serializable(with = LetterGradeSerializer::class) val grade: LetterGrade? = null,
)
