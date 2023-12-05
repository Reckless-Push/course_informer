package edu.umass.models

/**
 * Data class representing a user session.
 *
 * @property state The state of the user session.
 * @property token The token of the user session.
 */
data class UserSession(val state: String, val token: String)
