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
}
