package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * Represents a course with its details and associated information.
 *
 * @property cicsId The department-specific identifier for the course.
 * @property name The official name of the course.
 * @property description A brief summary of what the course entails.
 * @property credits The number of credit hours the course provides.
 * @property undergraduateRequirements A list of courses required as prerequisites for
 *   undergraduates, if any.
 * @property graduateRequirements A list of courses required as prerequisites for graduate students,
 *   if any.
 * @property semestersOffered A list of semesters during which the course is typically offered.
 * @property courseLevel The academic level of the course (e.g., 100-level, 200-level).
 * @property professors A list of professors who teach or have taught the course.
 */
@Serializable
data class Course(
    val cicsId: Int? = null,
    val name: String,
    val description: String,
    val credits: Int,
    val undergraduateRequirements: List<Course>?,
    val graduateRequirements: List<Course>?,
    val semestersOffered: List<Semester>?,
    val courseLevel: Int,
    val professors: List<Professor>?,
)
