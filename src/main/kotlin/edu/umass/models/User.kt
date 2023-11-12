package edu.umass.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val favoriteCourses: List<Course>,
    val reviews: List<Review>
)

object Users : Table() {
    private val id = integer("id").autoIncrement()
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)
    val email = varchar("email", 50)
    val favoriteCourses = varchar("favoriteCourses", 255)
    val reviews = varchar("reviews", 255)

    override val primaryKey = PrimaryKey(id)
}