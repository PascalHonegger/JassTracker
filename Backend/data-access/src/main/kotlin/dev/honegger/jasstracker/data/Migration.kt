package dev.honegger.jasstracker.data

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location

fun bootstrap(url: String, user: String, password: String?, runTestMigrations: Boolean) {
    val flyway = Flyway.configure().apply {
        dataSource(url, user, password)
        if (runTestMigrations) {
            locations(*locations, Location("classpath:db/migration_test"))
        }
    }.load()
    flyway.migrate()

    initializeDbConnection(url, user, password)
}
