package dev.honegger.jasstracker.domain

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Game(
    val id: UUID,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val rounds: List<Round>,
    val team1: Team,
    val team2: Team,
)

data class Team(
    val player1: GameParticipant,
    val player2: GameParticipant,
)

data class GameParticipant(
    val playerId: UUID,
    val displayName: String,
)
