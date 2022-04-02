package dev.honegger.domain

import java.util.UUID

data class UserSession(
    val userId: UUID,
    val username: String,
)
