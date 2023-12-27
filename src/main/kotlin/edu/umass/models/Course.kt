package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * Represents a new course with its details and associated information.
 *
 * @property id The Auto incremented course ID
 * @property cicsId The department-specific identifier for the course.
 * @property courseLevel The academic level of the course (e.g., 100-level, 200-level).
 * @property department The department that the course belongs to.
 * @property name The official name of the course.
 * @property description A brief summary of what the course entails.
 * @property credits The number of credit hours the course provides.
 * @property instructors A list of instructors who teach or have taught the course.
 * @property prerequisites A list of courses required as prerequisites, if any.
 * @property semestersOffered A list of semesters during which the course is typically offered.
 */
@Serializable
data class Course(
    val id: Int? = null,
    val cicsId: String,
    val courseLevel: Int,
    val department: String,
    val name: String,
    val description: String,
    val credits: Int,
    val instructors: List<Professor> = emptyList(),
    val prerequisites: String? = null,
    val semestersOffered: List<Semester> = emptyList(),
)
