package dev.honegger.plugins

import dev.honegger.migrations.runMigrations
import io.ktor.server.application.*

fun Application.runDbMigrations() {
    environment.config.apply {
        runMigrations(
            url = property("jasstracker.db.url").getString(),
            user = property("jasstracker.db.user").getString(),
            password = propertyOrNull("jasstracker.db.password")?.getString(),
            runTestMigrations = environment.developmentMode
        )
    }
}
