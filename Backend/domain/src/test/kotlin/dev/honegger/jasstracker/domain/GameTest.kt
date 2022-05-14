package dev.honegger.jasstracker.domain

import kotlinx.datetime.LocalDateTime
import java.util.*
import kotlin.test.*

class GameTest {
    private val anna = GameParticipation(UUID.randomUUID(), "Anna")
    private val andrin = GameParticipation(UUID.randomUUID(), "Andrin")
    private val joe = GameParticipation(UUID.randomUUID(), "Joe")
    private val jan = GameParticipation(UUID.randomUUID(), "Jan")
    private val baseGame = Game(
        id = UUID.randomUUID(),
        startTime = LocalDateTime(2022, 4, 23, 15, 23),
        team1 = Team(anna, andrin),
        team2 = Team(joe, jan),
        rounds = emptyList(),
    )

    private fun getCurrentPlayer(playerRounds: Map<GameParticipation, Int>) = baseGame.copy(
        rounds = playerRounds
            .flatMap { (participation, numRounds) -> List(numRounds) { participation } }
            .mapIndexed { index, gameParticipation ->
                Round(
                    id = UUID.randomUUID(),
                    number = index + 1,
                    score = 150,
                    gameId = baseGame.id,
                    playerId = gameParticipation.playerId,
                    contractId = UUID.randomUUID(),
                )
            }
    ).currentPlayer

    @Test
    fun `currentPlayer with no rounds returns anna`() = assertEquals(anna, getCurrentPlayer(emptyMap()))

    @Test
    fun `currentPlayer with 1 round returns joe`() = assertEquals(joe, getCurrentPlayer(mapOf(
        anna to 1,
    )))

    @Test
    fun `currentPlayer with 2 rounds returns andrin`() = assertEquals(andrin, getCurrentPlayer(mapOf(
        anna to 2,
    )))

    @Test
    fun `currentPlayer with 3 rounds returns jan`() = assertEquals(jan, getCurrentPlayer(mapOf(
        anna to 2,
        joe to 1,
    )))

    @Test
    fun `currentPlayer with 4 rounds returns anna`() = assertEquals(anna, getCurrentPlayer(mapOf(
        anna to 2,
        andrin to 1,
        joe to 1,
    )))

    @Test
    fun `currentPlayer with 17 rounds returns joe`() = assertEquals(joe, getCurrentPlayer(mapOf(
        anna to 4,
        andrin to 5,
        joe to 4,
        jan to 4,
    )))

    @Test
    fun `currentPlayer with 18 rounds returns andrin`() = assertEquals(andrin, getCurrentPlayer(mapOf(
        anna to 4,
        andrin to 5,
        joe to 4,
        jan to 5,
    )))

    @Test
    fun `currentPlayer with 17 rounds and team1 done returns joe`() = assertEquals(joe, getCurrentPlayer(mapOf(
        anna to 2,
        andrin to 8,
        joe to 4,
        jan to 3,
    )))

    @Test
    fun `currentPlayer with 18 rounds and team1 done returns jan`() = assertEquals(jan, getCurrentPlayer(mapOf(
        anna to 2,
        andrin to 8,
        joe to 4,
        jan to 4,
    )))

    @Test
    fun `currentPlayer with 19 rounds and team1 done returns joe`() = assertEquals(joe, getCurrentPlayer(mapOf(
        anna to 2,
        andrin to 8,
        joe to 5,
        jan to 4,
    )))

    @Test
    fun `currentPlayer with 18 rounds and team2 done returns andrin`() = assertEquals(andrin, getCurrentPlayer(mapOf(
        anna to 4,
        andrin to 4,
        joe to 2,
        jan to 8,
    )))

    @Test
    fun `currentPlayer with 19 rounds and team2 done returns anna`() = assertEquals(anna, getCurrentPlayer(mapOf(
        anna to 4,
        andrin to 5,
        joe to 2,
        jan to 8,
    )))

    @Test
    fun `currentPlayer with 20 rounds returns anna`() = assertEquals(anna, getCurrentPlayer(mapOf(
        anna to 5,
        andrin to 5,
        joe to 8,
        jan to 2,
    )))
}
