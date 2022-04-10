package dev.honegger.endpoints

import dev.honegger.services.GameService
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
                val updatedGame = call.receive<WebGame>().toGame()
                gameService.updateGame(dummySession, updatedGame)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
