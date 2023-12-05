package edu.umass.models

/**
 * Enumeration of letter grades.
 *
 * @property grade The letter grade.
 */
enum class LetterGrade(val grade: String) {
    GRADE_A("A"),
    GRADE_A_MINUS("A-"),
    GRADE_B_PLUS("B+"),
    GRADE_B("B"),
    GRADE_B_MINUS("B-"),
    GRADE_C_PLUS("C+"),
    GRADE_C("C"),
    GRADE_C_MINUS("C-"),
    GRADE_D_PLUS("D+"),
    GRADE_D("D"),
    GRADE_F("F"),
    ;

    /**
     * Returns a string representation of the letter grade.
     *
     * @return A string representing the letter grade.
     */
    override fun toString(): String = grade

    companion object {
        /**
         * Returns the letter grade corresponding to the given string.
         *
         * @param gradeString The string representation of the letter grade.
         * @return The letter grade corresponding to the given string, or null if no such grade exists.
         */
        fun fromString(gradeString: String): LetterGrade? =
            entries.firstOrNull { it.grade == gradeString }
    }
}
