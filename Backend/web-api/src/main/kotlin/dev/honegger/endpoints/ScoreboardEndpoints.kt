package dev.honegger.endpoints

import dev.honegger.domain.Scoreboard
import dev.honegger.domain.UserSession
import dev.honegger.services.ScoreboardService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// These objects could be generated using something like OpenAPI
@kotlinx.serialization.Serializable
data class WebScoreboard(val name: String, val ownerId: String)

@kotlinx.serialization.Serializable
data class WebCreateScoreboard(val name: String)

// Would be loaded once authentication is implemented
private val dummySession = UserSession(userId = "42", "dummy")

// Map from WebScoreboard to domain Scoreboard
private fun WebScoreboard.toScoreboard(id: String) = Scoreboard(
    id = id,
    name = name,
    ownerId = ownerId,
)

// Map from domain Scoreboard to WebScoreboard
private fun Scoreboard.toWebScoreboard() = WebScoreboard(
    name = name,
    ownerId = ownerId,
)

fun Application.configureScoreboardEndpoints(
    // Could be injected by a DI framework like Koin
    scoreboardService: ScoreboardService,
) {
    routing {
        get("/api/scoreboard/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val scoreboard =
                scoreboardService.getScoreboardOrNull(dummySession, id)

            if (scoreboard == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(HttpStatusCode.OK, scoreboard.toWebScoreboard())
        }
        put("/api/scoreboard") {
            val newScoreboard = call.receive<WebCreateScoreboard>()
            val createdScoreboard = scoreboardService.createScoreboard(
                dummySession,
                newScoreboard.name
            )
            call.respond(HttpStatusCode.Created, createdScoreboard.id)
        }
        post("/api/scoreboard/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val updatedScoreboard = call.receive<WebScoreboard>().toScoreboard(id)
            scoreboardService.updateScoreboard(dummySession, updatedScoreboard)
            call.respond(HttpStatusCode.Created)
        }
    }
}
