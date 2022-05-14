package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.domain.services.RoundService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureRoundEndpoints(
    roundService: RoundService,
) {
    route("/rounds") {
        get("/byGame/{gameId}") {
            val gameId = call.parameters["gameId"]
            if (gameId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val rounds = roundService.getRounds(call.playerSession(), gameId.toUUID())
            call.respond(HttpStatusCode.OK, rounds.map { it.toWebRound() })
        }
        post {
            val newRound = call.receive<WebCreateRound>()
            val createdRound = roundService.createRound(
                call.playerSession(),
                newRound.number,
                newRound.score,
                newRound.gameId,
                newRound.playerId,
                newRound.contractId
            )
            call.respond(HttpStatusCode.Created, createdRound.toWebRound())
        }
        put("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val updatedRound = call.receive<WebRound>().toRound()
            roundService.updateRound(call.playerSession(), updatedRound)
            call.respond(HttpStatusCode.OK)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val success = roundService.deleteRoundById(call.playerSession(), id.toUUID())
            if (!success) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
