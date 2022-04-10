package dev.honegger.domain

import java.util.UUID

data class Player(
    val id: UUID,
    val username: String?,
    val displayName: String?,
    val password: String?,
    val isGuest: Boolean
) {
    override fun toString(): String {
        return "Player(id=$id, username='$username', displayName='$displayName', password='<redacted>', isGuest=$isGuest)"
    }
}
