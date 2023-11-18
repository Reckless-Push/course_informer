/**
 * Define the UserSession class for authentication during a session.
 *
 * @file UserSession.kt
 * @version 0.1
 */

 package edu.umass.plugins

 data class UserSession(val state: String, val token: String)
