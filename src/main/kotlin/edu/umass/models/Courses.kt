package edu.umass.models

import org.jetbrains.exposed.sql.Table

object Courses : Table() {
    val cicsId = integer("cicsId")
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val credits = integer("credits")
    val undergraduateRequirements = varchar("undergraduateRequirements", 255).nullable()
    val graduateRequirements = varchar("graduateRequirements", 255).nullable()
    val semestersOffered = varchar("semestersOffered", 255).nullable()
    val courseLevel = integer("courseLevel")
    val professors = varchar("professors", 255).nullable()

    override val primaryKey = PrimaryKey(cicsId)
}