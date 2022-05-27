package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.api.util.respondNullable
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
            checkNotNull(id)
            val player = playerService.getPlayerOrNull(call.playerSession(), id.toUUID())
            call.respondNullable(player?.toWebPlayer())
        }
        put("/{id}/displayName") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val (displayName) = call.receive<DisplayNameRequest>()
            val token = playerService.updatePlayerDisplayName(call.playerSession(), displayName)
            call.respond(token.toTokenResponse())
        }
        put("/{id}/password") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val (oldPassword, newPassword) = call.receive<PasswordChangeRequest>()
            val token = playerService.updatePlayerPassword(call.playerSession(), oldPassword, newPassword)
            if (token == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            call.respond(token.toTokenResponse())
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)

            playerService.deletePlayer(call.playerSession(), id.toUUID())
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
