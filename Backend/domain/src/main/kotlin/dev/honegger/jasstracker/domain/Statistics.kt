package dev.honegger.jasstracker.domain

import java.util.*

@JvmInline
value class Average(val average: Double)

@JvmInline
value class Score(val score: Int)

data class TeamAverages(
    val team1: Average,
    val team2: Average,
)

data class PlayerAverage(
    val participation: GameParticipation,
    val average: Average,
    val weightedAverage: Average,
)

data class TeamScores(
    val team1: Score?,
    val team2: Score?,
)

data class GameScoreSummary(
    val gameId: UUID,
    val total: TeamScores,
)

data class ScoreDistributionItem(
    val score: Score,
    val height: Double,
    val occurrences: Int,
)

data class GameStatistics(
    val playerAverages: List<PlayerAverage>,
    val teamAverages: TeamAverages,
    val weightedTeamAverages: TeamAverages,
    val expectedScoresOverTime: List<TeamScores>,
)

data class TableStatistics(
    val playerAverages: List<PlayerAverage>,
    val contractAverages: Map<UUID, Average>,
    val scoresOverTime: List<GameScoreSummary>,
)

data class PlayerStatistics(
    val average: Average,
    val total: Score,
    val contractAverages: Map<UUID, Average>,
    val scoreDistribution: List<ScoreDistributionItem>,
)
