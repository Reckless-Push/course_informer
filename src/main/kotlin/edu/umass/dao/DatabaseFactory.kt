package edu.umass.dao

import edu.umass.models.Courses
import edu.umass.models.Professors
import edu.umass.models.Reviews
import edu.umass.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

import kotlinx.coroutines.Dispatchers

/** Provides functionality for initializing and interacting with the database. */
object DatabaseFactory {
    /** Initializes the database connection and creates the schema. */
    fun init() {
        // Database connection parameters
        val driverClassName = "org.h2.Driver"
        val jdbcUrl = "jdbc:h2:file:./build/db"

        // Establish connection to the database
        val database = Database.connect(jdbcUrl, driverClassName)

        // Transaction to create database tables for each entity if they don't exist
        transaction(database) {
            SchemaUtils.create(Courses)
            SchemaUtils.create(Professors)
            SchemaUtils.create(Users)
            SchemaUtils.create(Reviews)
        }
    }

    /**
     * Runs a database query within a coroutine transaction block on the IO dispatcher, providing
     * thread confinement for the suspension.
     *
     * @param block The suspend lambda containing the database operation to execute.
     * @return The result of the database query block.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
