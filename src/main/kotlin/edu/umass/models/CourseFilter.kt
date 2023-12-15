package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * Represents a filter for courses.
 *
 * @property minCredits The minimum number of credits for the course.
 * @property maxCredits The maximum number of credits for the course.
 * @property courseLevel The academic level of the course (e.g., 100-level, 200-level).
 * @property semestersOffered A list of semesters during which the course should be offered.
 */
@Serializable
data class CourseFilter(
    val minCredits: Int? = null,
    val maxCredits: Int? = null,
    val courseLevel: Int? = null,
    val semestersOffered: List<Semester>? = null,
)
