package edu.umass.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Professor(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val courses: List<Course>
)

object Professors : Table() {
    private val id = integer("id").autoIncrement()
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)

    override val primaryKey = PrimaryKey(id)
}