package edu.umass.models

import org.jetbrains.exposed.sql.Table

/** Defines the schema for the "Courses" table in the database. */
object Courses : Table() {
    // Unique identifier for the course, used as the primary key in the database.
    val id = integer("id").autoIncrement()

    // Unique identifier for the course, used as the primary key in the database.
    val cicsId = varchar("cics_id", MAX_CHAR)

    // Academic level of the course (e.g., 100 for freshman level, 200 for sophomore level, etc.).
    val courseLevel = integer("course_level")

    // 
    val department = varchar("course_department", MAX_CHAR)

    // Name of the course, stored as a variable character string with a maximum length of 255.
    val name = varchar("course_name", MAX_CHAR)

    // Description of the course content, stored as a variable character string with a maximum length
    // of 255.
    val description = text("description")

    // Boolean indicating whether the course is an undergraduate course.
    val undergrad = bool("undergrad")

    // Number of credits that the course is worth.
    val credits = integer("credits")

    // Comma-separated list of professor IDs who teach or have taught the course, nullable if there
    // are none.
    val instructors = varchar("instructors", MAX_CHAR).nullable()

    // Comma-separated list of undergraduate course IDs that are prerequisites for this course,
    // nullable if there are none.
    val prerequisites = text("prerequisites").nullable()

    // Comma-separated list of semesters during which the course is offered, nullable if not
    // applicable.
    val semestersOffered = varchar("semesters_offered", MAX_CHAR).nullable()

    // Declares the primary key of the table to be the 'cicsId' field.
    override val primaryKey = PrimaryKey(id)
}
