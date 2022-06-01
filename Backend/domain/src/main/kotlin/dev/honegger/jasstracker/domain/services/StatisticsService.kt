package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.repositories.ContractRepository
import dev.honegger.jasstracker.domain.repositories.RoundRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import dev.honegger.jasstracker.domain.util.scoreRange
import java.util.*
import kotlin.math.roundToInt

interface StatisticsService {
    fun getPlayerStatistics(playerSession: PlayerSession, playerId: UUID): PlayerStatistics
    fun getGameStatistics(playerSession: PlayerSession, gameId: UUID): GameStatistics
    fun getTableStatistics(playerSession: PlayerSession, tableId: UUID): TableStatistics
}

class StatisticsServiceImpl(
    private val roundRepository: RoundRepository,
    private val tableRepository: TableRepository,
    private val contractRepository: ContractRepository,
) : StatisticsService {

    // We assume the multiplier don't change during the runtime of one JassTracker instance
    private val multipliers by lazy { contractRepository.getContracts().associate { it.id to it.multiplier } }
    private val totalMultipliers by lazy { multipliers.values.sum() }

    private fun List<Round>.averageScore() =
        if (isEmpty()) Average(0.0) else Average(map { it.score }.average())

    private fun List<Round>.weightedAverageScore() =
        if (isEmpty()) Average(0.0) else Average(totalScore().score.toDouble() / sumOf { multipliers[it.contractId]!!.toDouble() })

    private fun List<Round>.predictedTotalScore() =
        if (isEmpty()) null else Score((weightedAverageScore().average * totalMultipliers).roundToInt())

    private fun List<Round>.totalScore() = Score(sumOf { it.score * multipliers[it.contractId]!! })
    private fun List<Game>.allRounds() = flatMap { it.rounds }

    override fun getPlayerStatistics(playerSession: PlayerSession, playerId: UUID): PlayerStatistics {
        val roundsByPlayer = roundRepository.getAllRoundsByPlayer(playerId)
        return PlayerStatistics(
            average = roundsByPlayer.averageScore(),
            total = roundsByPlayer.totalScore(),
            contractAverages = getContractAverages(roundsByPlayer),
            scoreDistribution = getScoreDistribution(roundsByPlayer),
        )
    }

    override fun getGameStatistics(playerSession: PlayerSession, gameId: UUID): GameStatistics {
        val table = tableRepository.getTableByGameIdOrNull(gameId)
        checkNotNull(table)
        val game = table.games.single()

        return GameStatistics(
            playerAverages = getPlayerAverages(table.games),
            teamAverages = getTeamAverages(game),
            weightedTeamAverages = getWeightedTeamAverages(game),
            expectedScoresOverTime = getExpectedScoreOverTime(game),
        )
    }

    override fun getTableStatistics(playerSession: PlayerSession, tableId: UUID): TableStatistics {
        val table = tableRepository.getTableOrNull(tableId)
        checkNotNull(table)

        val chronologicalGames = table.games.sortedBy { it.startTime }
        return TableStatistics(
            playerAverages = getPlayerAverages(chronologicalGames),
            contractAverages = getContractAverages(chronologicalGames.allRounds()),
            scoresOverTime = getScoresOverTime(chronologicalGames),
        )
    }

    private fun getExpectedScoreOverTime(game: Game): List<TeamScores> = buildList {
        val chronologicalRounds = game.rounds.sortedBy { it.number }
        val team1Rounds = mutableListOf<Round>()
        val team2Rounds = mutableListOf<Round>()

        chronologicalRounds.forEach {
            if (it.playerId in game.team1) {
                team1Rounds += it
            } else {
                team2Rounds += it
            }

            this += TeamScores(
                team1 = team1Rounds.predictedTotalScore(),
                team2 = team2Rounds.predictedTotalScore(),
            )
        }

        if (chronologicalRounds.size != Game.totalRounds) {
            val team1Score = team1Rounds.predictedTotalScore()
            val team2Score = team2Rounds.predictedTotalScore()
            repeat(Game.totalRounds - chronologicalRounds.size) {
                this += TeamScores(
                    team1 = team1Score,
                    team2 = team2Score,
                )
            }
        }
    }

    private fun getTeamAverages(game: Game): TeamAverages {
        val (team1Rounds, team2Rounds) = game.rounds.partition { it.playerId in game.team1 }
        return TeamAverages(team1Rounds.averageScore(), team2Rounds.averageScore())
    }

    private fun getWeightedTeamAverages(game: Game): TeamAverages {
        val (team1Rounds, team2Rounds) = game.rounds.partition { it.playerId in game.team1 }
        return TeamAverages(team1Rounds.weightedAverageScore(), team2Rounds.weightedAverageScore())
    }

    private fun getTeamScores(game: Game): TeamScores {
        val (team1Rounds, team2Rounds) = game.rounds.partition { it.playerId in game.team1 }
        return TeamScores(team1Rounds.totalScore(), team2Rounds.totalScore())
    }

    private fun getScoresOverTime(chronologicalGames: Iterable<Game>) = chronologicalGames.map {
        GameScoreSummary(
            gameId = it.id,
            total = getTeamScores(it),
        )
    }

    private val spread = 6
    private fun getScoreDistribution(rounds: List<Round>): List<ScoreDistributionItem> {
        if (rounds.isEmpty())
            return emptyList()

        val numberOfPossibleScores = scoreRange.last + 1
        val heights = DoubleArray(numberOfPossibleScores)
        val occurrences = IntArray(numberOfPossibleScores)
        rounds.forEach { round ->
            var addedHeight = 1.0
            val centerIndex = round.score
            heights[centerIndex] += addedHeight
            for(offset in 1..spread) {
                addedHeight /= 2.0
                val leftIndex = centerIndex - offset
                val rightIndex = centerIndex + offset
                if (leftIndex in heights.indices) {
                    heights[leftIndex] += addedHeight
                }
                if (rightIndex in heights.indices) {
                    heights[rightIndex] += addedHeight
                }
            }

            occurrences[round.score]++
        }
        val maxOccurrence = occurrences.maxOf { it }.toDouble()
        val maxHeight = heights.maxOf { it }

        return (0..157).map {
            ScoreDistributionItem(
                score = Score(it),
                // Scale height to max occurrence
                height = maxOccurrence * heights[it] / maxHeight,
                occurrences = occurrences[it]
            )
        }
    }

    private fun getContractAverages(rounds: List<Round>) = rounds
        .groupBy { it.contractId }
        .mapValues { (_, rounds) -> rounds.averageScore() }

    private fun getPlayerAverages(chronologicalGames: List<Game>): List<PlayerAverage> {
        val gameParticipation = buildMap {
            chronologicalGames.forEach {
                put(it.team1.player1.playerId, it.team1.player1)
                put(it.team1.player2.playerId, it.team1.player2)
                put(it.team2.player1.playerId, it.team2.player1)
                put(it.team2.player2.playerId, it.team2.player2)
            }
        }

        return chronologicalGames
            .allRounds()
            .groupBy { it.playerId }
            .map { (playerId, rounds) ->
                PlayerAverage(
                    participation = gameParticipation[playerId]!!,
                    average = rounds.averageScore(),
                    weightedAverage = rounds.weightedAverageScore()
                )
            }
    }
}
