package edu.umass.models

import org.jetbrains.exposed.sql.Table

/** Defines the schema for the "Users" table in the database. */
object Users : Table() {
    // Unique identifier for the user, used as the primary key in the database.
    val id = integer("id").autoIncrement()

    // First name of the user, stored as a variable character string with a maximum length of 50.
    val firstName = varchar("firstName", MAX_CHAR_NAME)

    // Last name of the user, stored as a variable character string with a maximum length of 50.
    val lastName = varchar("lastName", MAX_CHAR_NAME)

    // Email address of the user, stored as a variable character string with a maximum length of 50.
    val email = varchar("email", MAX_CHAR_NAME)

    // Comma-separated list of reviews written by the user, nullable if there are none.
    val reviews = varchar("reviews", MAX_CHAR).nullable()

    // Comma-separated list of courses favorited by the user, nullable if there are none.
    val favoriteCourses = varchar("favoriteCourses", MAX_CHAR).nullable()

    // Declares the primary key of the table to be the 'id' field.
    override val primaryKey = PrimaryKey(id)
}
