package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Player

@JvmInline
value class AuthToken(val token: String)

interface AuthTokenService {
    fun createToken(player: Player): AuthToken
}
