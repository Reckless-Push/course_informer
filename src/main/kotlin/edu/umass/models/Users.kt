package edu.umass.models

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement()
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)
    val email = varchar("email", 50)
    val reviews = varchar("reviews", 255).nullable()
    val favoriteCourses = varchar("favoriteCourses", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}
