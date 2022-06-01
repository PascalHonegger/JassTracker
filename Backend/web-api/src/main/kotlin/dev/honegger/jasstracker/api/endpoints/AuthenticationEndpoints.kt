package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.services.PlayerService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Route.configureAuthenticationEndpoints(
    playerService: PlayerService,
) {
    post("/login") {
        val login = call.receive<WebLogin>()
        val token = playerService.authenticatePlayer(login.username, login.password)
        call.respond(token.toTokenResponse())
    }

    post("/register") {
        val newPlayer = call.receive<WebCreatePlayer>()
        val token = playerService.registerPlayer(
            displayName = newPlayer.displayName,
            username = newPlayer.username,
            password = newPlayer.password,
        )
        call.respond(token.toTokenResponse())
    }

    post("/guest-access") {
        val token = playerService.registerGuestPlayer()
        call.respond(token.toTokenResponse())
    }
}
