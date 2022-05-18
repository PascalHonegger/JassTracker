package dev.honegger.jasstracker.api.util

import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

fun ApplicationCall.playerSession(): PlayerSession =
    playerSessionOrNull() ?: error("No valid JWT Principal found, cannot get user session")

fun ApplicationCall.playerSessionOrNull(): PlayerSession? = principal<JWTPrincipal>()?.payload?.let {
    val userId: UUID = it.getClaim(PlayerSession::playerId.name).asString()?.toUUID() ?: return@let null
    val isGuest: Boolean = it.getClaim(PlayerSession::isGuest.name).asBoolean() ?: error("isGuest claim missing for user $userId")
    val username = it.getClaim(PlayerSession::username.name).asString().takeIf { !isGuest }

    PlayerSession(
        playerId = userId,
        isGuest = isGuest,
        username = username,
    )
}
