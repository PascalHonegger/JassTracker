package dev.honegger.jasstracker.domain

import java.util.UUID

// Important!
// Property names could be used for serialization / storage within a token
// Make sure any refactoring is backwards compatible
data class PlayerSession(
    val playerId: UUID,
    val isGuest: Boolean,
    val username: String?,
    val displayName: String?,
)
