package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.domain.services.StatisticsService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureStatisticsEndpoint(
    statisticsService: StatisticsService,
) {
    route("/statistics") {
        get("/game/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val gameStatistics = statisticsService.getGameStatistics(call.playerSession(), id.toUUID())
            call.respond(HttpStatusCode.OK, gameStatistics.toWebGameStatistics())
        }
        get("/player/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val playerStatistics = statisticsService.getPlayerStatistics(call.playerSession(), id.toUUID())
            call.respond(HttpStatusCode.OK, playerStatistics.toWebPlayerStatistics())
        }
        get("/table/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val tableStatistics = statisticsService.getTableStatistics(call.playerSession(), id.toUUID())
            call.respond(HttpStatusCode.OK, tableStatistics.toWebTableStatistics())
        }
    }
}
