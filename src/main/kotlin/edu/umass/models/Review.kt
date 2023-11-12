package edu.umass.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: Int,
    val professor: Professor,
    val course: Course,
    val user: User,
    val date: LocalDateTime,
    val difficulty: Int,
    val quality: Int,
    val tags: List<String>?,
    val comment: String,
    val fromRMP: Boolean
)