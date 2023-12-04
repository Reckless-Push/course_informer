package edu.umass.models

/**
 * Enumeration of seasons during which a course can be offered.
 *
 * @property season The name of the season.
 */
enum class SemesterSeason(val season: String) {
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall"),
    WINTER("Winter"),
    ;

    /**
     * Creates a formatted string representing the semester with a given year.
     *
     * @param year The academic year to be associated with the season.
     * @return A string in the format "Season Year" (e.g., "Spring 2023").
     */
    fun format(year: Int) = "$season $year"

    companion object {
        /**
         * Creates a SemesterSeason from a given string.
         *
         * @param seasonString The string representation of the season.
         * @return The corresponding SemesterSeason enum instance.
         * @throws IllegalArgumentException If the string does not match any season.
         */
        fun fromString(seasonString: String): SemesterSeason? =
            entries.firstOrNull { it.season.equals(seasonString, ignoreCase = true) }
    }
}
