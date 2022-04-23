package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.services.PlayerService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configurePlayerEndpoints(
    playerService: PlayerService,
) {
    routing {
        route("/api/players") {
            get("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val player =
                    playerService.getPlayerOrNull(dummySession, UUID.fromString(id))

                if (player == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(HttpStatusCode.OK, player.toWebPlayer())
            }
            post {
                val newPlayer = call.receive<WebCreatePlayer>()
                val createdPlayer = playerService.createPlayer(
                    session = dummySession,
                    displayName = newPlayer.displayName,
                    username = newPlayer.username,
                    password = newPlayer.password,
                )
                call.respond(HttpStatusCode.Created, createdPlayer.toWebPlayer())
            }
            put("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }
                when (val updatedPlayer = call.receive<WebPlayer>().toPlayer()) {
                    is GuestPlayer -> {
                        call.respond(HttpStatusCode.BadRequest, "Cannot update guest player")
                    }
                    is RegisteredPlayer -> {
                        playerService.updatePlayer(dummySession, updatedPlayer)
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}
