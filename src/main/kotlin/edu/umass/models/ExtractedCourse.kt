package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * @property cicsId
 * @property department
 * @property name
 * @property description
 * @property credits
 * @property instructors
 * @property prerequisites
 */
@Serializable
data class ExtractedCourse(
    val cicsId: String,
    val department: String,
    val name: String,
    val description: String,
    val credits: Int,
    val instructors: List<String>,
    val prerequisites: String,
)
