package edu.umass.models

import org.jetbrains.exposed.sql.Table

/** Defines the schema for the "Professors" table in the database. */
object Professors : Table() {
    // Unique identifier for the professor, used as the primary key in the database.
    val id = integer("id").autoIncrement()

    // First name of the professor, stored as a variable character string with a maximum length of 50.
    val firstName = varchar("first_name", MAX_CHAR_NAME)

    // Last name of the professor, stored as a variable character string with a maximum length of 50.
    val lastName = varchar("last_name", MAX_CHAR_NAME)

    // Declares the primary key of the table to be the 'id' field.
    override val primaryKey = PrimaryKey(id)
}
