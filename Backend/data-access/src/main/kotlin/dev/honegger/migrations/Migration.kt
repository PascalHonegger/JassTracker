package dev.honegger.migrations

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location

fun runMigrations(url: String, user: String, password: String?, runTestMigrations: Boolean) {
    val flyway = Flyway.configure().apply {
        dataSource(url, user, password)
        if (runTestMigrations) {
            locations(*locations, Location("classpath:db/migration_test"))
        }
    }.load()
    flyway.migrate()
}
