package dev.honegger.jasstracker.domain

import java.util.UUID

sealed interface Player {
    val id: UUID
}

data class RegisteredPlayer(
    override val id: UUID,
    val username: String,
    var displayName: String,
    val password: String,
): Player {
    override fun toString(): String {
        return "RegisteredPlayer(id=$id, username='$username', displayName='$displayName', password='<redacted>')"
    }
}

data class GuestPlayer(
    override val id: UUID,
): Player
