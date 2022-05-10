package dev.honegger.jasstracker.api.util

import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun ApplicationCall.userSession(): PlayerSession = principal<JWTPrincipal>()?.payload?.let {
    PlayerSession(
        userId = it.getClaim("PlayerId").asString()?.toUUID() ?: TODO(),
        username = if (it.getClaim("IsGuest").asBoolean()) {
            it.getClaim("Username").asString() ?: TODO()
        } else null
    )
} ?: error("No JWT Principal found, cannot get user session")