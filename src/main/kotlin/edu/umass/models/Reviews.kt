package edu.umass.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

/** Defines the schema for the "Reviews" table in the database. */
object Reviews : Table() {
    // Unique identifier for the review, used as the primary key in the database.
    val id = integer("id").autoIncrement()

    // Foreign key to the 'id' field of the 'Professors' table.
    val professorId = reference("professor_id", Professors.id)

    // Foreign key to the 'cicsId' field of the 'Courses' table.
    val courseId = reference("course_id", Courses.cicsId)

    // Foreign key to the 'id' field of the 'Users' table.
    val userId = reference("user_id", Users.id)

    // Date and time that the review was submitted, stored as a datetime object.
    val datetime = datetime("date")

    // Difficulty rating given by the user.
    val difficulty = integer("difficulty")

    // Quality rating given by the user.
    val quality = integer("quality")

    // Comma-separated list of tags given by the user.
    val tags = varchar("tags", MAX_CHAR).nullable()

    // Comment written by the user, stored as a variable character string with a maximum length of
    // 255.
    val comment = varchar("comment", MAX_CHAR)

    // Whether the review was imported from RateMyProfessor.
    val fromRmp = bool("from_rmp")
    override val primaryKey = PrimaryKey(id)
}
