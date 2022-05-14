package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.services.AuthTokenService
import dev.honegger.jasstracker.domain.services.PlayerService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Route.configureAuthenticationEndpoints(
    playerService: PlayerService,
    authTokenService: AuthTokenService,
) {
    post("/login") {
        val login = call.receive<WebLogin>()

        val player = playerService.authenticatePlayer(login.username, login.password)
        if (player == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val token = authTokenService.createToken(player)
        call.respond(hashMapOf("token" to token))
    }

    post("/guest-access") {
        val player = playerService.registerGuestPlayer()
        val token = authTokenService.createToken(player)
        call.respond(hashMapOf("token" to token))
    }
}
