package edu.umass.models

import java.util.Calendar
import kotlinx.serialization.Serializable

/**
 * Data class representing a specific semester in which a course might be offered. This includes
 * both the season and the year, providing a complete designation of the semester.
 *
 * @property season The season of the semester (e.g., Spring, Summer, Fall, Winter).
 * @property year The calendar year of the semester.
 */
@Serializable
data class Semester(val season: SemesterSeason, val year: Int) {
    init {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        require(year in OLDEST_YEAR..currentYear + 1) {
            "Year must be within the valid range (1900 - ${currentYear + 1})"
        }
    }

    /**
     * Returns a string representation of the semester. Overrides the default `toString` to output a
     * string in the format defined by `SemesterSeason.format`.
     *
     * @return A string representing the semester, such as "Fall 2023".
     */
    override fun toString() = season.format(year)
}
