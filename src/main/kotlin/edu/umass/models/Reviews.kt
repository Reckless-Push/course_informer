package edu.umass.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Reviews : Table() {
    val id = integer("id").autoIncrement()
    val professorId = reference("professor_id", Professors.id)
    val courseId = reference("course_id", Courses.cicsId)
    val userId = reference("user_id", Users.id)
    val datetime = datetime("date")
    val difficulty = integer("difficulty")
    val quality = integer("quality")
    val tags = varchar("tags", 255)
    val comment = varchar("comment", 255)
    val fromRMP = bool("fromRMP")

    override val primaryKey = PrimaryKey(id)
}