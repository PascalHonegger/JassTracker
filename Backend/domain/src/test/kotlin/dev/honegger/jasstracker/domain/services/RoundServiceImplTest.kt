package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.repositories.RoundRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import io.mockk.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.*

class RoundServiceImplTest {
    private val roundRepository = mockk<RoundRepository>()
    private val tableRepository = mockk<TableRepository>()
    private val service = RoundServiceImpl(roundRepository, tableRepository)
    private val dummySession = UserSession(UUID.randomUUID(), "dummy")
    private val passedRound = slot<Round>()

    @BeforeTest
    fun setup() {
        clearMocks(roundRepository, tableRepository)
        passedRound.clear()
        every { roundRepository.saveRound(capture(passedRound)) } just Runs
    }

    @AfterTest
    fun teardown() {
        confirmVerified(roundRepository, tableRepository)
    }

    @Test
    fun `createRound creates round in repository`() {
        val dummyNum = 1
        val dummyScore = 150
        val dummyGame = UUID.randomUUID()
        val dummyPlayer = UUID.randomUUID()
        val dummyContract = UUID.randomUUID()

        every { tableRepository.getTableByGameIdOrNull(dummyGame) } returns Table(
            id = UUID.randomUUID(),
            name = "Foo",
            ownerId = dummyPlayer,
            games = listOf(
                Game(
                    id = dummyGame,
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    rounds = emptyList(),
                    team1 = Team(GameParticipant(UUID.randomUUID(), "p1"), GameParticipant(UUID.randomUUID(), "p2")),
                    team2 = Team(GameParticipant(UUID.randomUUID(), "p3"), GameParticipant(UUID.randomUUID(), "p4")),
                )
            )
        )

        val created = service.createRound(dummySession, dummyNum, dummyScore, dummyGame, dummyPlayer, dummyContract)

        assertTrue { passedRound.isCaptured }
        assertEquals(created, passedRound.captured)
        assertEquals(dummyNum, created.number)
        assertEquals(dummyScore, created.score)
        assertEquals(dummyGame, created.gameId)
        assertEquals(dummyPlayer, created.playerId)
        assertEquals(dummyContract, created.contractId)

        verify(exactly = 1) {
            roundRepository.saveRound(any())
            tableRepository.getTableByGameIdOrNull(dummyGame)
        }
    }

    @Test
    fun `createRound fails with score over 157`() {
        val thrown = assertThrows<IllegalStateException> {
            service.createRound(
                session = dummySession,
                number = 0,
                score = 158,
                gameId = UUID.randomUUID(),
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID())
        }
        assertEquals("Score must be between 0 and 157", thrown.message)
    }

    @Test
    fun `createRound fails with score below 0`() {
        val thrown = assertThrows<IllegalStateException> {
            service.createRound(
                session = dummySession,
                number = 0,
                score = -1,
                gameId = UUID.randomUUID(),
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID())
        }
        assertEquals("Score must be between 0 and 157", thrown.message)
    }

    @Test
    fun `getRounds returns all rounds for a game`() {
        val dummyGame = UUID.randomUUID()
        every { roundRepository.getRoundsForGame(dummyGame) } returns emptyList()
        assertEquals(emptyList(), service.getRounds(dummySession, dummyGame))
        verify(exactly = 1) { roundRepository.getRoundsForGame(dummyGame) }
    }
}
