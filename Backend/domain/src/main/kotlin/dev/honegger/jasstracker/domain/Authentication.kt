package dev.honegger.jasstracker.domain

import java.util.UUID

data class UserSession(
    val userId: UUID,
    val username: String,
    val token: String,
)
