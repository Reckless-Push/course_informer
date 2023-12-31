package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * Represents a filter for courses.
 *
 * @property credits A list of credits to select.
 * @property undergrad A boolean indicating whether to select undergraduate courses.
 * @property grad A boolean indicating whether to select graduate courses.
 * @property semestersOffered A list of semesters during which the course should be offered.
 */
@Serializable
data class CourseFilter(
    val credits: List<Int> = emptyList(),
    val undergrad: Boolean = false,
    val grad: Boolean = false,
    val semestersOffered: List<Semester> = emptyList(),
)
