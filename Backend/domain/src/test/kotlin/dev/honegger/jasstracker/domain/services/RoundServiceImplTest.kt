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
    private val dummySession = PlayerSession(UUID.randomUUID(), false, "dummy", "Dummy")
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
                    team1 = Team(GameParticipation(UUID.randomUUID(), "p1"), GameParticipation(UUID.randomUUID(), "p2")),
                    team2 = Team(GameParticipation(UUID.randomUUID(), "p3"), GameParticipation(UUID.randomUUID(), "p4")),
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
    fun `deleteRoundById removes a created round`() {
        val dummyRound = Round(UUID.randomUUID(), 1, 157, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        every {
            roundRepository.getRoundOrNull(dummyRound.id)
        } returns dummyRound

        every {
            roundRepository.deleteRoundById(dummyRound.id)
        } returns true

        val deleted = service.deleteRoundById(dummySession, dummyRound.id)
        assertTrue { deleted }
        verify(exactly = 1) {
            roundRepository.getRoundOrNull(dummyRound.id)
            roundRepository.deleteRoundById(dummyRound.id)
        }
    }

    @Test
    fun `deleteRoundById removes a round, table can't be found afterwards anymore`() {
        val roundId = UUID.randomUUID()
        val dummyRound = Round(roundId, 1, 157, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())

        every {
            roundRepository.getRoundOrNull(roundId)
        } returns dummyRound

        every {
            roundRepository.deleteRoundById(roundId)
        } returns false

        val deleted = service.deleteRoundById(dummySession, roundId)
        assertFalse { deleted }

        verify(exactly = 1) {
            roundRepository.deleteRoundById(roundId)
            roundRepository.getRoundOrNull(roundId)
        }
    }

    @Test
    fun `updateRound updates score`() {
        val id = UUID.randomUUID()
        val oldRound = Round(id, 1, 80, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val updatedRound = oldRound.copy(score = 95)

        every {
            roundRepository.getRoundOrNull(id)
        } returns oldRound

        every {
            roundRepository.updateRound(updatedRound)
        } just Runs

        service.updateRound(dummySession, updatedRound)

        verify (exactly = 1) {
            roundRepository.getRoundOrNull(id)
            roundRepository.updateRound(updatedRound)
        }
    }

    @Test
    fun `updateRound throws if round`() {
        val id = UUID.randomUUID()
        val oldRound = Round(id, 1, 80, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val updatedRound = oldRound.copy(score = 95)

        every {
            roundRepository.getRoundOrNull(id)
        } returns oldRound

        every {
            roundRepository.updateRound(updatedRound)
        } just Runs

        service.updateRound(dummySession, updatedRound)

        verify (exactly = 1) {
            roundRepository.getRoundOrNull(id)
            roundRepository.updateRound(updatedRound)
        }
    }
}
