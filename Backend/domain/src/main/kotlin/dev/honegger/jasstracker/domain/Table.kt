package dev.honegger.jasstracker.domain

import java.util.UUID

data class Table(
    val id: UUID,
    val name: String,
    val ownerId: UUID,
    val games: List<Game>,
)
