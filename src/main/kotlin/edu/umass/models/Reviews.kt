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
    val userId = reference("user_id", Users.uuid).nullable()

    // Date and time that the review was submitted, stored as a datetime object.
    val datetime = datetime("review_date").nullable()

    // Difficulty rating given by the user.
    val difficulty = integer("difficulty")

    // Quality rating given by the user.
    val quality = integer("quality")

    // Comment written by the user.
    val comment = varchar("comment", MAX_CHAR)

    // Whether the review was imported from RateMyProfessor.
    val fromRmp = bool("from_rmp")

    // Whether the review was for credit.
    val forCredit = bool("for_credit")

    // Whether the review required attendance.
    val attendance = bool("attendance")

    // Whether the review required a textbook.
    val textbook = bool("textbook")

    // The grade the user received in the course.
    val grade = varchar("grade", MAX_CHAR).nullable()

    // Set the primary key of the table to be the 'id' field.
    override val primaryKey = PrimaryKey(id)
}
