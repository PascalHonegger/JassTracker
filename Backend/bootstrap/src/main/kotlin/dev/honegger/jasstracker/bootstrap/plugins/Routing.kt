package dev.honegger.jasstracker.bootstrap.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureStaticRouting() {
    routing {
        singlePageApplication {
            vue("static")
            useResources = true
        }
    }
}
