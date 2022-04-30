package dev.honegger.jasstracker.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import java.util.*
import kotlin.test.*

class TableTest {
    private val baseTable = Table(
        id = UUID.randomUUID(),
        name = "Test Table",
        games = emptyList(),
        ownerId = UUID.randomUUID(),
    )
    private val dummyTeam1 = Team(
        player1 = GameParticipation(UUID.randomUUID(), "Dummy 1"),
        player2 = GameParticipation(UUID.randomUUID(), "Dummy 2"),
    )
    private val dummyTeam2 = Team(
        player1 = GameParticipation(UUID.randomUUID(), "Dummy 3"),
        player2 = GameParticipation(UUID.randomUUID(), "Dummy 4"),
    )

    private fun dummyGame(startMonth: Month, endMonth: Month? = null) = Game(
        id = UUID.randomUUID(),
        startTime = LocalDateTime(2022, startMonth, 1, 2, 3),
        endTime = endMonth?.let { LocalDateTime(2022, it, 1, 2, 3) },
        rounds = emptyList(),
        team1 = dummyTeam1,
        team2 = dummyTeam2,
    )

    private val januaryToApril = dummyGame(Month.JANUARY, Month.APRIL)
    private val februaryToMarch = dummyGame(Month.FEBRUARY, Month.MARCH)
    private val januaryNoEnd = dummyGame(Month.JANUARY)
    private val februaryNoEnd = dummyGame(Month.FEBRUARY)

    private fun getLatestGame(vararg games: Game) = baseTable.copy(games = games.toList()).latestGame

    @Test
    fun `latestGame with no games returns null`() =
        assertNull(getLatestGame())

    @Test
    fun `latestGame with open games returns the one with latest start date`() =
        assertEquals(februaryNoEnd, getLatestGame(januaryNoEnd, februaryNoEnd))

    @Test
    fun `latestGame with finished games returns one with latest start date`() =
        assertEquals(februaryToMarch, getLatestGame(februaryToMarch, januaryToApril))

    @Test
    fun `latestGame with finished and open games returns open one with latest start`() =
        assertEquals(februaryNoEnd, getLatestGame(januaryNoEnd, februaryNoEnd, februaryToMarch, januaryToApril))
}
