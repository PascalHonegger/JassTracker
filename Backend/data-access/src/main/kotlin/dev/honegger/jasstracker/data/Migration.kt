package dev.honegger.jasstracker.data

import org.flywaydb.core.Flyway

fun bootstrap(url: String, user: String, password: String?, runTestMigrations: Boolean) {
    val flyway = Flyway.configure().apply {
        dataSource(url, user, password)
        if (runTestMigrations) {
            locations("classpath:db/migration", "classpath:db/migration_test")
        } else {
            locations("classpath:db/migration")
        }
    }.load()
    flyway.migrate()

    initializeDbConnection(url, user, password)
}
