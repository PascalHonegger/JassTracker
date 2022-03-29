package dev.honegger.domain

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Game(
    val id: UUID,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null
)
