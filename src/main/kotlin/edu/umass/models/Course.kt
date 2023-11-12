package edu.umass.models

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val cicsId: Int,
    val name: String,
    val description: String,
    val credits: Int,
    val undergraduateRequirements: List<Course>?,
    val graduateRequirements: List<Course>?,
    val semestersOffered: List<String>?,
    val courseLevel: Int,
    val professors: List<Professor>?,
)
