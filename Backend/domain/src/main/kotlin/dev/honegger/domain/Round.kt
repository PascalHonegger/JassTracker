package dev.honegger.domain

import java.util.UUID

data class Round(
    val id: UUID,
    val number: Int,
    val score: Int,
    val gameId: UUID,
    val playerId: UUID,
    val contractId: UUID
)
