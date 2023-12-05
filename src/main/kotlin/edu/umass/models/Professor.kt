package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * Represents a professor with their details and associated information.
 *
 * @property id The unique identifier for the professor.
 * @property firstName The professor's first name.
 * @property lastName The professor's last name.
 */
@Serializable
data class Professor(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Professor) {
            return true
        }

        return id == other.id && firstName == other.firstName && lastName == other.lastName
    }
}
