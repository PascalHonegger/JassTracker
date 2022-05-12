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
        val newUserSession = call.receive<WebCreatePlayer>()

        val player = playerService.authenticatePlayer(newUserSession.username, newUserSession.password)
        if (player == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val token = authTokenService.createToken(player)
        call.respond(token)
    }

    post("/guestAccess") {
        val player = playerService.registerGuestPlayer()
        val token = authTokenService.createToken(player)
        call.respond(hashMapOf("token" to token))
    }
}