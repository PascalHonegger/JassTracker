package dev.honegger.migrations

import org.flywaydb.core.Flyway

fun runMigrations(url: String, user: String, password: String?) {
    val flyway = Flyway.configure().dataSource(url, user, password).load()
    flyway.migrate()
}
