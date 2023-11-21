package edu.umass.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a user session.
 *
 * @property id The user's ID.
 * @property name The user's name.
 * @property givenName The user's given name.
 * @property familyName The user's family name.
 * @property picture The user's profile picture.
 * @property locale The user's locale.
 * @property email The user's email.
 * @property verifiedEmail Whether the user's email is verified.
 * @property hd The user's hosted domain.
 */
@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    @SerialName("family_name") val familyName: String,
    val picture: String,
    val locale: String,
    val email: String,
    @SerialName("verified_email") val verifiedEmail: Boolean,
    val hd: String,
)
