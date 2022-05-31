package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.repositories.ContractRepository
import dev.honegger.jasstracker.domain.repositories.RoundRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import io.mockk.*
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StatisticsServiceImplTest {
    private val roundRepository = mockk<RoundRepository>()
    private val tableRepository = mockk<TableRepository>()
    private val contractRepository = mockk<ContractRepository>()
    private val service = StatisticsServiceImpl(roundRepository, tableRepository, contractRepository)
    private val tableId = UUID.randomUUID()
    private val gameId = UUID.randomUUID()
    private val anna = GameParticipation(UUID.randomUUID(), "Anna")
    private val andrin = GameParticipation(UUID.randomUUID(), "Andrin")
    private val joe = GameParticipation(UUID.randomUUID(), "Joe")
    private val jan = GameParticipation(UUID.randomUUID(), "Jan")
    private val dummySession = PlayerSession(UUID.randomUUID(), false, "dummy", "Dummy")

    private val contracts = listOf(
        Contract(UUID.randomUUID(), "Eichle", 1, ContractType.Acorns),
        Contract(UUID.randomUUID(), "Rose", 2, ContractType.Roses),
        Contract(UUID.randomUUID(), "Schilte", 3, ContractType.Shields),
        Contract(UUID.randomUUID(), "Schälle", 4, ContractType.Bells),
        Contract(UUID.randomUUID(), "Obenabe", 5, ContractType.TopsDown),
        Contract(UUID.randomUUID(), "Undenufe", 6, ContractType.BottomsUp),
        Contract(UUID.randomUUID(), "Joker", 7, ContractType.Joker),
        Contract(UUID.randomUUID(), "Joker", 8, ContractType.Joker),
        Contract(UUID.randomUUID(), "Slalom", 9, ContractType.Slalom),
        Contract(UUID.randomUUID(), "Guschti", 10, ContractType.Guschti),
    )

    private fun makeRound(number: Int, score: Int, player: GameParticipation, multiplier: Int) = Round(
        id = UUID.randomUUID(),
        number = number,
        score = score,
        gameId = gameId,
        playerId = player.playerId,
        contractId = contracts.single { it.multiplier == multiplier }.id
    )

    private val rounds = listOf(
        makeRound(number = 1, score = 157, player = anna, multiplier = 3),
        makeRound(number = 2, score = 78, player = jan, multiplier = 3),
        makeRound(number = 3, score = 137, player = andrin, multiplier = 1),
        makeRound(number = 4, score = 139, player = joe, multiplier = 4),
        makeRound(number = 5, score = 93, player = andrin, multiplier = 9),
        makeRound(number = 6, score = 157, player = joe, multiplier = 10),
        makeRound(number = 7, score = 124, player = anna, multiplier = 6),
        makeRound(number = 8, score = 136, player = andrin, multiplier = 7),
        makeRound(number = 9, score = 60, player = anna, multiplier = 2),
        makeRound(number = 10, score = 84, player = andrin, multiplier = 5),
        makeRound(number = 11, score = 99, player = andrin, multiplier = 10),
        makeRound(number = 12, score = 85, player = jan, multiplier = 1),
        makeRound(number = 13, score = 119, player = anna, multiplier = 8),
        makeRound(number = 14, score = 78, player = jan, multiplier = 2),
        makeRound(number = 15, score = 44, player = anna, multiplier = 4),
        makeRound(number = 16, score = 48, player = joe, multiplier = 5),
        makeRound(number = 17, score = 136, player = jan, multiplier = 8),
        makeRound(number = 18, score = 147, player = jan, multiplier = 9),
        makeRound(number = 19, score = 108, player = jan, multiplier = 7),
        makeRound(number = 20, score = 94, player = joe, multiplier = 6),
    )

    private fun List<PlayerAverage>.getAverage(participation: GameParticipation) =
        single { it.participation == participation }.average.average

    private fun List<PlayerAverage>.getWeightedAverage(participation: GameParticipation) =
        single { it.participation == participation }.weightedAverage.average

    @BeforeTest
    fun setup() {
        clearMocks(roundRepository, tableRepository, contractRepository)
        every { contractRepository.getContracts() } returns contracts
    }

    @AfterTest
    fun teardown() {
        confirmVerified(roundRepository, tableRepository)
    }

    @Nested
    inner class GameStatistics {
        @Test
        fun `get game statistics for inexistent game throws`() {
            every { tableRepository.getTableByGameIdOrNull(gameId) } returns null
            assertThrows<IllegalStateException> {
                service.getGameStatistics(dummySession, gameId)
            }
            verify(exactly = 1) {
                tableRepository.getTableByGameIdOrNull(gameId)
            }
        }

        @Test
        fun `get game statistics for finished game`() {
            val finishedGame = Game(
                id = gameId,
                startTime = LocalDateTime(2022, 6, 28, 15, 21),
                endTime = LocalDateTime(2022, 6, 28, 18, 36),
                rounds = rounds,
                team1 = Team(anna, andrin),
                team2 = Team(joe, jan),
            )
            every { tableRepository.getTableByGameIdOrNull(gameId) } returns Table(
                id = tableId,
                name = "Test Table",
                ownerId = UUID.randomUUID(),
                games = listOf(finishedGame)
            )
            val gameStatistics = service.getGameStatistics(dummySession, gameId)

            assertEquals(100.8, gameStatistics.playerAverages.getAverage(anna), absoluteTolerance = 0.001)
            assertEquals(109.8, gameStatistics.playerAverages.getAverage(andrin), absoluteTolerance = 0.001)
            assertEquals(109.5, gameStatistics.playerAverages.getAverage(joe), absoluteTolerance = 0.001)
            assertEquals(105.333, gameStatistics.playerAverages.getAverage(jan), absoluteTolerance = 0.001)

            assertEquals(107.087, gameStatistics.playerAverages.getWeightedAverage(anna), absoluteTolerance = 0.001)
            assertEquals(104.25, gameStatistics.playerAverages.getWeightedAverage(andrin), absoluteTolerance = 0.001)
            assertEquals(117.2, gameStatistics.playerAverages.getWeightedAverage(joe), absoluteTolerance = 0.001)
            assertEquals(121.4, gameStatistics.playerAverages.getWeightedAverage(jan), absoluteTolerance = 0.001)

            assertEquals(105.3, gameStatistics.teamAverages.team1.average, absoluteTolerance = 0.001)
            assertEquals(107.0, gameStatistics.teamAverages.team2.average, absoluteTolerance = 0.001)

            assertEquals(105.436, gameStatistics.weightedTeamAverages.team1.average, absoluteTolerance = 0.001)
            assertEquals(119.49, gameStatistics.weightedTeamAverages.team2.average, absoluteTolerance = 0.001)

            assertEquals(20, gameStatistics.expectedScoresOverTime.size)
            assertEquals(Score(157 * 55), gameStatistics.expectedScoresOverTime[0].team1)
            assertEquals(null, gameStatistics.expectedScoresOverTime[0].team2)
            assertEquals(Score(157 * 55), gameStatistics.expectedScoresOverTime[1].team1)
            assertEquals(Score(78 * 55), gameStatistics.expectedScoresOverTime[1].team2)
            assertEquals(Score(5799), gameStatistics.expectedScoresOverTime.last().team1)
            assertEquals(Score(6572), gameStatistics.expectedScoresOverTime.last().team2)

            verify(exactly = 1) {
                tableRepository.getTableByGameIdOrNull(gameId)
            }
        }

        @Test
        fun `get game statistics for running game`() {
            val runningGame = Game(
                id = gameId,
                startTime = LocalDateTime(2022, 6, 28, 15, 21),
                endTime = null,
                rounds = rounds.take(10),
                team1 = Team(anna, andrin),
                team2 = Team(joe, jan),
            )
            every { tableRepository.getTableByGameIdOrNull(gameId) } returns Table(
                id = tableId,
                name = "Test Table",
                ownerId = UUID.randomUUID(),
                games = listOf(runningGame)
            )
            val gameStatistics = service.getGameStatistics(dummySession, gameId)

            assertEquals(113.666, gameStatistics.playerAverages.getAverage(anna), absoluteTolerance = 0.001)
            assertEquals(112.5, gameStatistics.playerAverages.getAverage(andrin), absoluteTolerance = 0.001)
            assertEquals(148.0, gameStatistics.playerAverages.getAverage(joe), absoluteTolerance = 0.001)
            assertEquals(78.0, gameStatistics.playerAverages.getAverage(jan), absoluteTolerance = 0.001)

            assertEquals(113.0, gameStatistics.teamAverages.team1.average, absoluteTolerance = 0.001)
            assertEquals(124.666, gameStatistics.teamAverages.team2.average, absoluteTolerance = 0.001)

            assertEquals(111.545, gameStatistics.weightedTeamAverages.team1.average, absoluteTolerance = 0.001)
            assertEquals(138.824, gameStatistics.weightedTeamAverages.team2.average, absoluteTolerance = 0.001)

            assertEquals(20, gameStatistics.expectedScoresOverTime.size)
            assertEquals(Score(157 * 55), gameStatistics.expectedScoresOverTime[0].team1)
            assertEquals(null, gameStatistics.expectedScoresOverTime[0].team2)
            assertEquals(Score(157 * 55), gameStatistics.expectedScoresOverTime[1].team1)
            assertEquals(Score(78 * 55), gameStatistics.expectedScoresOverTime[1].team2)
            assertEquals(Score(6135), gameStatistics.expectedScoresOverTime[9].team1)
            assertEquals(Score(7635), gameStatistics.expectedScoresOverTime[9].team2)
            assertEquals(Score(6135), gameStatistics.expectedScoresOverTime.last().team1)
            assertEquals(Score(7635), gameStatistics.expectedScoresOverTime.last().team2)

            verify(exactly = 1) {
                tableRepository.getTableByGameIdOrNull(gameId)
            }
        }
    }

    @Nested
    inner class PlayerStatistics {
        @Test
        fun `get player statistics without rounds is empty`() {
            every { roundRepository.getAllRoundsByPlayer(any()) } returns emptyList()
            val playerStatistics = service.getPlayerStatistics(dummySession, gameId)

            assertEquals(0.0, playerStatistics.average.average, absoluteTolerance = 0.001)
            assertEquals(0, playerStatistics.total.score)
            assertEquals(emptyMap(), playerStatistics.contractAverages)
            assertEquals(emptyList(), playerStatistics.scoreDistribution)

            verify(exactly = 1) {
                roundRepository.getAllRoundsByPlayer(any())
            }
        }

        @Test
        fun `get player statistics`() {
            val dummyRounds = listOf(
                makeRound(1, 100, anna, 2),
                makeRound(2, 25, anna, 1),
                makeRound(3, 50, anna, 1),
                makeRound(3, 50, anna, 3),
            )
            every { roundRepository.getAllRoundsByPlayer(anna.playerId) } returns dummyRounds

            val annaStatistics = service.getPlayerStatistics(dummySession, anna.playerId)
            assertEquals(56.25, annaStatistics.average.average, absoluteTolerance = 0.001)
            assertEquals(425, annaStatistics.total.score)
            assertEquals(
                expected = 100.0,
                actual = annaStatistics.contractAverages[contracts.single { it.multiplier == 2 }.id]!!.average,
                absoluteTolerance = 0.001
            )
            assertEquals(
                expected = 37.5,
                actual = annaStatistics.contractAverages[contracts.single { it.multiplier == 1 }.id]!!.average,
                absoluteTolerance = 0.001
            )
            assertEquals(
                ScoreDistributionItem(score = Score(0), height = 0.0, occurrences = 0),
                annaStatistics.scoreDistribution[0]
            )
            assertEquals(
                ScoreDistributionItem(score = Score(50), height = 2.0, occurrences = 2),
                annaStatistics.scoreDistribution[50]
            )
            assertEquals(
                ScoreDistributionItem(score = Score(100), height = 1.0, occurrences = 1),
                annaStatistics.scoreDistribution[100]
            )

            verify(exactly = 1) {
                roundRepository.getAllRoundsByPlayer(any())
            }
        }
    }

    @Nested
    inner class TableStatistics {
        @Test
        fun `get player statistics without rounds is empty`() {
            val tableId = UUID.randomUUID()
            val game1 = Game(
                id = UUID.randomUUID(),
                startTime = LocalDateTime(2022, 6, 28, 15, 21),
                endTime = LocalDateTime(2022, 6, 28, 18, 36),
                rounds = rounds,
                team1 = Team(anna, andrin),
                team2 = Team(joe, jan),
            )
            val game2 = Game(
                id = UUID.randomUUID(),
                startTime = LocalDateTime(2022, 6, 29, 15, 21),
                endTime = null,
                rounds = rounds.take(10),
                team1 = Team(anna, andrin),
                team2 = Team(joe, jan),
            )
            every { tableRepository.getTableOrNull(tableId) } returns Table(
                id = tableId,
                name = "Test Table",
                ownerId = UUID.randomUUID(),
                games = listOf(game1, game2)
            )
            val tableStatistics = service.getTableStatistics(dummySession, tableId)

            assertEquals(105.625, tableStatistics.playerAverages.getAverage(anna), absoluteTolerance = 0.001)
            assertEquals(111.0, tableStatistics.playerAverages.getAverage(andrin), absoluteTolerance = 0.001)
            assertEquals(122.333, tableStatistics.playerAverages.getAverage(joe), absoluteTolerance = 0.001)
            assertEquals(101.429, tableStatistics.playerAverages.getAverage(jan), absoluteTolerance = 0.001)

            assertEquals(
                expected = 119.667,
                actual = tableStatistics.contractAverages[contracts.single { it.multiplier == 1 }.id]!!.average,
                absoluteTolerance = 0.001
            )

            assertEquals(
                expected = listOf(
                    GameScoreSummary(game1.id, TeamScores(team1 = Score(5799), team2 = Score(6572))),
                    GameScoreSummary(game2.id, TeamScores(team1 = Score(3681), team2 = Score(2360))),
                ),
                actual = tableStatistics.scoresOverTime,
            )

            verify(exactly = 1) {
                tableRepository.getTableOrNull(tableId)
            }
        }
    }
}
