package edu.umass.models

import kotlinx.serialization.Serializable

@Serializable
data class Professor(
    val id: Int,
    val firstName: String,
    val lastName: String,
)
