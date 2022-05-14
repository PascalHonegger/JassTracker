package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.domain.currentPlayer
import dev.honegger.jasstracker.domain.services.GameService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureGameEndpoints(
    gameService: GameService,
) {
    route("/games") {
        get {
            val games = gameService.getAllGames(call.playerSession())
            call.respond(HttpStatusCode.OK, games.map { it.toWebGame() })
        }
        get("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val game =
                gameService.getGameOrNull(call.playerSession(), id.toUUID())

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
                gameService.getGameOrNull(call.playerSession(), id.toUUID())

            if (game == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(HttpStatusCode.OK, game.currentPlayer.toWebGameParticipation())
        }
        post {
            val newGame = call.receive<WebCreateGame>()
            fun CreateGameParticipation(game: WebCreateGameParticipation) =
                dev.honegger.jasstracker.domain.services.CreateGameParticipation(game.playerId, game.displayName)

            val createdGame = gameService.createGame(
                call.playerSession(),
                newGame.tableId.toUUID(),
                CreateGameParticipation(newGame.team1Player1),
                CreateGameParticipation(newGame.team1Player2),
                CreateGameParticipation(newGame.team2Player1),
                CreateGameParticipation(newGame.team2Player2),
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
            gameService.updateGame(call.playerSession(), updatedGame)
            call.respond(HttpStatusCode.OK)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val success = gameService.deleteGameById(call.playerSession(), id.toUUID())
            if (!success) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
