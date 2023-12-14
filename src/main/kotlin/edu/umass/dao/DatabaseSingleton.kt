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
object DatabaseSingleton {
    /** Initializes the database connection and creates the schema. */
    fun init() {
        // Read database configuration parameters
        val dbUser = System.getenv("POSTGRES_USER")
        val dbPassword = System.getenv("POSTGRES_PASSWORD")
        val driverClassName = System.getenv("JDBC_DRIVER")
        val jdbcUrl = System.getenv("JDBC_URL")

        // Establish connection to the database
        val database =
            if (dbUser != null && dbPassword != null && !driverClassName.equals("org.h2.Driver")) {
                Database.connect(jdbcUrl, driverClassName, dbUser, dbPassword)
            } else {
                Database.connect(jdbcUrl, driverClassName)
            }

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
     * thread confinement for the suspension.D
     *
     * @param block The suspend lambda containing the database operation to execute.
     * @return The result of the database query block.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
