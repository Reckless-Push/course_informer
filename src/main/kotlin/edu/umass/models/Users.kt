package edu.umass.models

import org.jetbrains.exposed.sql.Table

/**
 * Defines the schema for the "Users" table in the database.
 */
object Users : Table() {
    // Unique identifier for the user, used as the primary key in the database.
    val id = integer("id").autoIncrement()

    // First name of the user, stored as a variable character string with a maximum length of 50.
    val firstName = varchar("firstName", 50)

    // Last name of the user, stored as a variable character string with a maximum length of 50.
    val lastName = varchar("lastName", 50)

    // Email address of the user, stored as a variable character string with a maximum length of 50.
    val email = varchar("email", 50)

    // Comma-separated list of reviews written by the user, nullable if there are none.
    val reviews = varchar("reviews", 255).nullable()

    // Comma-separated list of courses favorited by the user, nullable if there are none.
    val favoriteCourses = varchar("favoriteCourses", 255).nullable()

    // Declares the primary key of the table to be the 'id' field.
    override val primaryKey = PrimaryKey(id)
}
