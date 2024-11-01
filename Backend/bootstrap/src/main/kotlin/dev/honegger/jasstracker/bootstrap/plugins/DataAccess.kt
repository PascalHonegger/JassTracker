package dev.honegger.jasstracker.bootstrap.plugins

import dev.honegger.jasstracker.data.bootstrap
import io.ktor.server.application.*

fun Application.initializeDatabase() {
    environment.config.apply {
        bootstrap(
            url = property("jasstracker.db.url").getString(),
            user = property("jasstracker.db.user").getString(),
            password = propertyOrNull("jasstracker.db.password")?.getString(),
            runTestMigrations = developmentMode
        )
    }
}
