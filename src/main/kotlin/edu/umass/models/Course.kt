package edu.umass.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Course(
    val id: Int,
    val cicsId: Int,
    val name: String,
    val description: String,
    val credits: Int,
    val undergraduateRequirements: List<Course>,
    val graduateRequirements: List<Course>,
    val semestersOffered: List<String>,
    val courseLevel: Int,
    val professors: List<Professor>
)

object Courses : Table() {
    private val id = integer("id").autoIncrement()
    val cicsId = integer("cicsId")
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val credits = integer("credits")
    val undergraduateRequirements = varchar("undergraduateRequirements", 255)
    val graduateRequirements = varchar("graduateRequirements", 255)
    val semestersOffered = varchar("semestersOffered", 255)
    val courseLevel = integer("courseLevel")
    val professors = varchar("professors", 255)

    override val primaryKey = PrimaryKey(id)
}
