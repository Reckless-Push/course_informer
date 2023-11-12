package edu.umass.models

import org.jetbrains.exposed.sql.Table

/**
 * Defines the schema for the "Courses" table in the database.
 */
object Courses : Table() {
    // Unique identifier for the course, used as the primary key in the database.
    val cicsId = integer("cicsId").uniqueIndex()

    // Name of the course, stored as a variable character string with a maximum length of 255.
    val name = varchar("name", 255)

    // Description of the course content, stored as a variable character string with a maximum length of 255.
    val description = varchar("description", 255)

    // Number of credits that the course is worth.
    val credits = integer("credits")

    // Comma-separated list of undergraduate course IDs that are prerequisites for this course, nullable if there are none.
    val undergraduateRequirements = varchar("undergraduateRequirements", 255).nullable()

    // Comma-separated list of graduate course IDs that are prerequisites for this course, nullable if there are none.
    val graduateRequirements = varchar("graduateRequirements", 255).nullable()

    // Comma-separated list of semesters during which the course is offered, nullable if not applicable.
    val semestersOffered = varchar("semestersOffered", 255).nullable()

    // Academic level of the course (e.g., 100 for freshman level, 200 for sophomore level, etc.).
    val courseLevel = integer("courseLevel")

    // Comma-separated list of professor IDs who teach or have taught the course, nullable if there are none.
    val professors = varchar("professors", 255).nullable()

    // Declares the primary key of the table to be the 'cicsId' field.
    override val primaryKey = PrimaryKey(cicsId)
}