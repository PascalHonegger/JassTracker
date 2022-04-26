package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.currentPlayer
import dev.honegger.jasstracker.domain.services.GameService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configureGameEndpoints(
    gameService: GameService,
) {
    routing {
        route("/api/games") {
            get {
                val games = gameService.getAllGames(dummySession)
                call.respond(HttpStatusCode.OK, games.map { it.toWebGame() })
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
            get("/{id}/currentPlayer") {
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

                call.respond(HttpStatusCode.OK, game.currentPlayer.toWebGameParticipant())
            }
            post {
                val newGame = call.receive<WebCreateGame>()
                fun CreateGameParticipant(game: WebCreateGameParticipant) =
                    dev.honegger.jasstracker.domain.services.CreateGameParticipant(game.playerId, game.displayName)
                val createdGame = gameService.createGame(
                    dummySession,
                    UUID.fromString(newGame.tableId),
                    CreateGameParticipant(newGame.team1Player1),
                    CreateGameParticipant(newGame.team1Player2),
                    CreateGameParticipant(newGame.team2Player1),
                    CreateGameParticipant(newGame.team2Player2),
                )
                call.respond(HttpStatusCode.Created, createdGame.toWebGame())
            }
            put("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }
                val updatedGame = call.receive<WebGame>().toGame()
                gameService.updateGame(dummySession, updatedGame)
                call.respond(HttpStatusCode.OK)
            }
            delete("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                val success = gameService.deleteGameById(dummySession, UUID.fromString(id))
                if (!success) {
                    call.respond(HttpStatusCode.NotFound)
                    return@delete
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
