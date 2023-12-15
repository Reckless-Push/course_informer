package edu.umass.models

import kotlinx.serialization.Serializable

/**
 * CourseIngest is a data class that represents a request to ingest courses from a URL.
 *
 * @property url The URL from which to ingest courses.
 * @constructor Creates a new CourseIngest.
 */
@Serializable data class CourseIngest(val url: String)
