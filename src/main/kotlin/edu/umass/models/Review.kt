package edu.umass.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime


@Serializable
data class Review(
    val id: Int,
    val professor: Professor,
    val course: Course,
    val user: User,
    val date: LocalDateTime,
    val difficulty: Int,
    val quality: Int,
    val tags: List<String>,
    val comment: String,
    val fromRMP: Boolean
)

object Reviews : Table() {
    private val id = integer("id").autoIncrement()
    val professor = integer("professor")
    val course = integer("course")
    val user = integer("user")
    val datetime = datetime("date")
    val difficulty = integer("difficulty")
    val quality = integer("quality")
    val tags = varchar("tags", 255)
    val comment = varchar("comment", 255)
    val fromRMP = bool("fromRMP")

    override val primaryKey = PrimaryKey(id)
}