package dev.honegger.endpoints

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@kotlinx.serialization.Serializable
data class Serialization(val message: String)

fun Application.configureSampleEndpoints() {
    routing {
        get("/api/hello-world") {
            call.respond(Serialization("Hello World!"))
        }
    }
}
