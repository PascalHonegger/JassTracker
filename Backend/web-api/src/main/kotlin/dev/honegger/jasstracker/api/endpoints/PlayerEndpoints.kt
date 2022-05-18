package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.services.PlayerService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configurePlayerEndpoints(
    playerService: PlayerService,
) {
    route("/players") {
        get("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val player =
                playerService.getPlayerOrNull(call.playerSession(), id.toUUID())

            if (player == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(HttpStatusCode.OK, player.toWebPlayer())
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
                    playerService.updatePlayer(call.playerSession(), updatedPlayer)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
