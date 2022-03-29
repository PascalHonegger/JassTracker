package dev.honegger.endpoints

import dev.honegger.domain.Game
import dev.honegger.domain.UserSession
import dev.honegger.serializer.UUIDSerializer
import dev.honegger.services.GameService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

// These objects could be generated using something like OpenAPI
@Serializable
data class WebGame(
    val startTime: Instant,
    val endTime: Instant?,
)

@Serializable
data class WebGameWithId(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val startTime: Instant,
    val endTime: Instant?,
)

@Serializable
data class WebCreateGame(val tableId: String)

// Would be loaded once authentication is implemented
private val dummySession = UserSession(userId = UUID.fromString("2ea1cb74-a9aa-4b81-8953-d7a16d6ba582"), "dummy")

// Map from WebGame to domain Game
private fun WebGame.toGame(id: UUID) = Game(
    id = id,
    startTime = startTime.toLocalDateTime(TimeZone.UTC),
    endTime = endTime?.toLocalDateTime(TimeZone.UTC),
)

// Map from domain Game to WebGame
private fun Game.toWebGame() = WebGame(
    startTime = startTime.toInstant(TimeZone.UTC),
    endTime = endTime?.toInstant(TimeZone.UTC),
)

// Map from domain Game to WebGame
private fun Game.toWebGameWithId() = WebGameWithId(
    id = id,
    startTime = startTime.toInstant(TimeZone.UTC),
    endTime = endTime?.toInstant(TimeZone.UTC),
)

fun Application.configureGameEndpoints(
    // Could be injected by a DI framework like Koin
    gameService: GameService,
) {
    routing {
        route("/api/games") {
            get {
                val games = gameService.getAllGames(dummySession)
                call.respond(HttpStatusCode.OK, games.map { it.toWebGameWithId() })
            }
            get("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val game =
                    gameService.getGameOrNull(dummySession, UUID.fromString(id))

                if (game == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(HttpStatusCode.OK, game.toWebGame())
            }
            put {
                val newGame = call.receive<WebCreateGame>()
                val createdGame = gameService.createGame(
                    dummySession,
                    UUID.fromString(newGame.tableId),
                )
                call.respond(HttpStatusCode.Created, createdGame.id.toString())
            }
            post("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val updatedGame = call.receive<WebGame>().toGame(UUID.fromString(id))
                gameService.updateGame(dummySession, updatedGame)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
