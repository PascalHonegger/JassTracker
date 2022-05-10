package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Player

interface AuthTokenService {
    fun createToken(player: Player): String
}