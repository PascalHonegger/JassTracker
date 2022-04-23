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

operator fun Team.contains(playerId: UUID): Boolean = player1.playerId == playerId || player2.playerId == playerId

/**
 * Returns the players whose turn it is.
 * Start with team1 player1 and go "clockwise" from there.
 * If one team has completed all their rounds, only the players of the other team can be chosen.
 * If all rounds have been played, team1 player1 is returned.
 */
val Game.currentPlayer
    get(): GameParticipant = when (val playedRounds = rounds.size) {
        0, 20 -> team1.player1
        in 1..19 -> when {
            // Team 1 has played their 10 rounds, switch between the two remaining players from team 2
            rounds.count { it.playerId in team1 } == 10 -> when (playedRounds % 2) {
                1 -> team2.player1
                0 -> team2.player2
                else -> error("Number % 2 must be 0 or 1")
            }
            // Team 2 has played their 10 rounds, switch between the two remaining players from team 1
            rounds.count { it.playerId in team2 } == 10 -> when (playedRounds % 2) {
                1 -> team1.player1
                0 -> team1.player2
                else -> error("Number % 2 must be 0 or 1")
            }
            // No team has finished their rounds, go through all players
            else -> when (playedRounds % 4) {
                0 -> team1.player1
                1 -> team2.player1
                2 -> team1.player2
                3 -> team2.player2
                else -> error("Number % 4 must be between 0, 1, 2 or 3")
            }
        }
        else -> error("Game must have between 0 and 20 rounds, but game $id has ${rounds.size} rounds")
    }
