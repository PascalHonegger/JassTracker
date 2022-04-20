package dev.honegger.endpoints

import dev.honegger.services.RoundService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRoundEndpoints(
    roundService: RoundService,
) {
    routing {
        route("/api/rounds") {
            get("/byGame/{gameId}") {
                val gameId = call.parameters["gameId"]
                if (gameId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val rounds = roundService.getRounds(dummySession, UUID.fromString(gameId))
                call.respond(HttpStatusCode.OK, rounds.map { it.toWebRound() })
            }
            post {
                val newRound = call.receive<WebCreateRound>()
                val createdRound = roundService.createRound(
                    dummySession,
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
                roundService.updateRound(dummySession, updatedRound)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
