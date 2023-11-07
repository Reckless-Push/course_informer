package edu.umass.models

import org.jetbrains.exposed.sql.Table

object Professors : Table() {
    val id = integer("id").autoIncrement()
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)

    override val primaryKey = PrimaryKey(id)
}