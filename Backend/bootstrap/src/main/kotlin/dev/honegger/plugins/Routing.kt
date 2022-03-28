package dev.honegger.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureStaticRouting() {
    routing {
        static("/") {
            staticBasePackage = "static"
            resources(".")
            defaultResource("index.html")
        }
    }
}
