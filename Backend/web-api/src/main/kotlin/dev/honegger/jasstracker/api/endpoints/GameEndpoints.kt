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
            call.respond(games.map { it.toWebGame() })
        }
        get("/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val game = gameService.getGame(call.playerSession(), id.toUUID())
            call.respond(game.toWebGame())
        }
        get("/{id}/currentPlayer") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val game = gameService.getGame(call.playerSession(), id.toUUID())
            call.respond(game.currentPlayer.toWebGameParticipation())
        }
        post {
            val newGame = call.receive<WebCreateGame>()
            fun CreateGameParticipation(game: WebCreateGameParticipation) =
                dev.honegger.jasstracker.domain.services.CreateGameParticipation(game.playerId, game.displayName)

            val createdGame = gameService.createGame(
                call.playerSession(),
                newGame.tableId,
                CreateGameParticipation(newGame.team1Player1),
                CreateGameParticipation(newGame.team1Player2),
                CreateGameParticipation(newGame.team2Player1),
                CreateGameParticipation(newGame.team2Player2),
            )
            call.respond(HttpStatusCode.Created, createdGame.toWebGame())
        }
        put("/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val updatedGame = call.receive<WebGame>().toGame()
            gameService.updateGame(call.playerSession(), updatedGame)
            call.respond(HttpStatusCode.NoContent)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            gameService.deleteGameById(call.playerSession(), id.toUUID())
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
