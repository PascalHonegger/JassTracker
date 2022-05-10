package dev.honegger.jasstracker.domain

import java.util.UUID

data class PlayerSession(
    val userId: UUID,
    val username: String?,
)
