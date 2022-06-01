package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.exceptions.NotFoundException
import dev.honegger.jasstracker.domain.exceptions.UnauthorizedException
import dev.honegger.jasstracker.domain.repositories.ContractRepository
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
    private val contractRepository = mockk<ContractRepository>()
    private val service = RoundServiceImpl(roundRepository, tableRepository, contractRepository)
    private val dummySession = PlayerSession(UUID.randomUUID(), false, "dummy", "Dummy")
    private val passedRound = slot<Round>()

    @BeforeTest
    fun setup() {
        clearMocks(roundRepository, tableRepository)
        passedRound.clear()
        every { roundRepository.saveRound(capture(passedRound)) } just Runs
        every { contractRepository.contractExists(any()) } returns true
    }

    @AfterTest
    fun teardown() {
        confirmVerified(roundRepository, tableRepository, contractRepository)
    }

    @Test
    fun `createRound creates round in repository`() {
        val dummyNum = 1
        val dummyScore = 150
        val dummyGame = UUID.randomUUID()
        val dummyPlayer = dummySession.playerId
        val dummyContract = UUID.randomUUID()
        val dummyTable = UUID.randomUUID()

        every { tableRepository.getTableByGameIdOrNull(dummyGame) } returns Table(
            id = dummyTable,
            name = "Foo",
            ownerId = dummySession.playerId,
            games = listOf(
                Game(
                    id = dummyGame,
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    rounds = emptyList(),
                    team1 = Team(GameParticipation(dummyPlayer, "p1"), GameParticipation(dummySession.playerId, "p2")),
                    team2 = Team(
                        GameParticipation(UUID.randomUUID(), "p3"),
                        GameParticipation(UUID.randomUUID(), "p4")
                    ),
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
            contractRepository.contractExists(dummyContract)
            tableRepository.getTableByGameIdOrNull(dummyGame)
            roundRepository.saveRound(any())
        }
    }

    @Test
    fun `createRound fails with score over 157`() {
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                session = dummySession,
                number = 1,
                score = 158,
                gameId = UUID.randomUUID(),
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Score must be between 0..157", thrown.message)
    }

    @Test
    fun `createRound fails with score below 0`() {
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                session = dummySession,
                number = 1,
                score = -1,
                gameId = UUID.randomUUID(),
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Score must be between 0..157", thrown.message)
    }

    @Test
    fun `createRound fails with number below 1`() {
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                dummySession,
                number = 0,
                score = 100,
                gameId = UUID.randomUUID(),
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Number must be between 1..20", thrown.message)
    }

    @Test
    fun `createRound fails with number above 20`() {
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                dummySession,
                number = 21,
                score = 100,
                gameId = UUID.randomUUID(),
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Number must be between 1..20", thrown.message)
    }


    @Test
    fun `createRound throws if contract does not exist`() {
        val dummyContractId = UUID.randomUUID()
        every { contractRepository.contractExists(dummyContractId) } returns false
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                dummySession,
                number = 1,
                score = 100,
                gameId = UUID.randomUUID(),
                playerId = UUID.randomUUID(),
                contractId = dummyContractId
            )
        }
        assertEquals("Contract $dummyContractId does not exist", thrown.message)
        verify(exactly = 1) {
            contractRepository.contractExists(dummyContractId)
        }
    }

    @Test
    fun `createRound throws if game does not exist`() {
        val dummyGameId = UUID.randomUUID()
        every { tableRepository.getTableByGameIdOrNull(dummyGameId) } returns null
        val thrown = assertThrows<NotFoundException> {
            service.createRound(
                dummySession,
                number = 1,
                score = 100,
                gameId = dummyGameId,
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Game $dummyGameId does not exist", thrown.message)
        verify(exactly = 1) {
            contractRepository.contractExists(any())
            tableRepository.getTableByGameIdOrNull(dummyGameId)
        }
    }

    @Test
    fun `createRound throws if table is not owned`() {
        val dummyGameId = UUID.randomUUID()
        every { tableRepository.getTableByGameIdOrNull(dummyGameId) } returns Table(
            id = UUID.randomUUID(),
            name = "dummy",
            ownerId = UUID.randomUUID(),
            games = listOf(
                Game(
                    id = dummyGameId,
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    endTime = null,
                    team1 = Team(GameParticipation(dummySession.playerId, "T1P1"), GameParticipation(UUID.randomUUID(), "T1P2")),
                    team2 = Team(GameParticipation(UUID.randomUUID(), "T2P1"), GameParticipation(UUID.randomUUID(), "T2P2")),
                    rounds = emptyList()
                )
            )
        )
        val thrown = assertThrows<UnauthorizedException> {
            service.createRound(
                dummySession,
                number = 1,
                score = 100,
                gameId = dummyGameId,
                playerId = UUID.randomUUID(),
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Only table owner can add new rounds to game", thrown.message)
        verify(exactly = 1) {
            contractRepository.contractExists(any())
            tableRepository.getTableByGameIdOrNull(dummyGameId)
        }
    }

    @Test
    fun `createRound throws if player is not in game`() {
        val dummyGameId = UUID.randomUUID()
        val dummyPlayerId = UUID.randomUUID()
        every { tableRepository.getTableByGameIdOrNull(dummyGameId) } returns Table(
            id = UUID.randomUUID(),
            name = "dummy",
            ownerId = dummySession.playerId,
            games = listOf(
                Game(
                    id = dummyGameId,
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    endTime = null,
                    team1 = Team(GameParticipation(dummySession.playerId, "T1P1"), GameParticipation(UUID.randomUUID(), "T1P2")),
                    team2 = Team(GameParticipation(UUID.randomUUID(), "T2P1"), GameParticipation(UUID.randomUUID(), "T2P2")),
                    rounds = emptyList()
                )
            )
        )
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                dummySession,
                number = 1,
                score = 100,
                gameId = dummyGameId,
                playerId = dummyPlayerId,
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Player $dummyPlayerId is not in game $dummyGameId", thrown.message)
        verify(exactly = 1) {
            contractRepository.contractExists(any())
            tableRepository.getTableByGameIdOrNull(dummyGameId)
        }
    }

    @Test
    fun `createRound throws if round number is duplicate`() {
        val dummyGameId = UUID.randomUUID()
        val p1 = dummySession.playerId
        val p2 = UUID.randomUUID()
        val p3 = UUID.randomUUID()
        val p4 = UUID.randomUUID()
        every { tableRepository.getTableByGameIdOrNull(dummyGameId) } returns Table(
            id = UUID.randomUUID(),
            name = "dummy",
            ownerId = dummySession.playerId,
            games = listOf(
                Game(
                    id = dummyGameId,
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    endTime = null,
                    team1 = Team(GameParticipation(p1, "T1P1"), GameParticipation(p2, "T1P2")),
                    team2 = Team(GameParticipation(p3, "T2P1"), GameParticipation(p4, "T2P2")),
                    rounds = listOf(Round(id = UUID.randomUUID(), number = 1, score = 100, gameId = dummyGameId, playerId = p3, contractId = UUID.randomUUID()))
                )
            )
        )
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                dummySession,
                number = 1,
                score = 100,
                gameId = dummyGameId,
                playerId = p2,
                contractId = UUID.randomUUID()
            )
        }
        assertEquals("Round nr. 1 was already played", thrown.message)
        verify(exactly = 1) {
            contractRepository.contractExists(any())
            tableRepository.getTableByGameIdOrNull(dummyGameId)
        }
    }

    @Test
    fun `createRound throws if contract was already played by team member`() {
        val dummyGameId = UUID.randomUUID()
        val p1 = dummySession.playerId
        val p2 = UUID.randomUUID()
        val p3 = UUID.randomUUID()
        val p4 = UUID.randomUUID()
        val contractId = UUID.randomUUID()
        every { tableRepository.getTableByGameIdOrNull(dummyGameId) } returns Table(
            id = UUID.randomUUID(),
            name = "dummy",
            ownerId = dummySession.playerId,
            games = listOf(
                Game(
                    id = dummyGameId,
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    endTime = null,
                    team1 = Team(GameParticipation(p1, "T1P1"), GameParticipation(p2, "T1P2")),
                    team2 = Team(GameParticipation(p3, "T2P1"), GameParticipation(p4, "T2P2")),
                    rounds = listOf(Round(id = UUID.randomUUID(), number = 1, score = 100, gameId = dummyGameId, playerId = p1, contractId = contractId))
                )
            )
        )
        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(
                dummySession,
                number = 2,
                score = 100,
                gameId = dummyGameId,
                playerId = p2,
                contractId = contractId
            )
        }
        assertEquals("Contract $contractId was already played by team member", thrown.message)
        verify(exactly = 1) {
            contractRepository.contractExists(any())
            tableRepository.getTableByGameIdOrNull(dummyGameId)
        }
    }

    @Test
    fun `createRound throws if game is already completed`() {
        val dummyGameId = UUID.randomUUID()
        val p1 = dummySession.playerId
        val p2 = UUID.randomUUID()
        val p3 = UUID.randomUUID()
        val p4 = UUID.randomUUID()
        val contractId = UUID.randomUUID()
        every { tableRepository.getTableByGameIdOrNull(dummyGameId) } returns Table(
            id = UUID.randomUUID(),
            name = "dummy",
            ownerId = dummySession.playerId,
            games = listOf(
                Game(
                    id = dummyGameId,
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    endTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    team1 = Team(GameParticipation(p1, "T1P1"), GameParticipation(p2, "T1P2")),
                    team2 = Team(GameParticipation(p3, "T2P1"), GameParticipation(p4, "T2P2")),
                    rounds = emptyList()
                )
            )
        )

        val thrown = assertThrows<IllegalArgumentException> {
            service.createRound(dummySession, 1, 100, dummyGameId, p2, contractId)
        }
        assertEquals("Cannot add round to completed game", thrown.message)

        verify(exactly = 1) {
            contractRepository.contractExists(contractId)
            tableRepository.getTableByGameIdOrNull(dummyGameId)
        }
    }

    @Test
    fun `deleteRoundById removes a created round`() {
        val dummyRound = Round(UUID.randomUUID(), 1, 157, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        every {
            roundRepository.getRoundOrNull(dummyRound.id)
        } returns dummyRound

        every {
            tableRepository.getTableByGameIdOrNull(dummyRound.gameId)
        } returns createDummyTable(dummyRound, dummySession.playerId)

        every {
            roundRepository.deleteRoundById(dummyRound.id)
        } returns true

        val deleted = service.deleteRoundById(dummySession, dummyRound.id)
        assertTrue { deleted }
        verify(exactly = 1) {
            roundRepository.getRoundOrNull(dummyRound.id)
            tableRepository.getTableByGameIdOrNull(dummyRound.gameId)
            roundRepository.deleteRoundById(dummyRound.id)
        }
    }

    @Test
    fun `deleteRoundById throws if table is not owned`() {
        val dummyRound = Round(UUID.randomUUID(), 1, 157, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        every {
            roundRepository.getRoundOrNull(dummyRound.id)
        } returns dummyRound

        every {
            tableRepository.getTableByGameIdOrNull(dummyRound.gameId)
        } returns createDummyTable(dummyRound, UUID.randomUUID())

        every {
            roundRepository.deleteRoundById(dummyRound.id)
        } returns true

        val thrown = assertThrows<UnauthorizedException> {
            service.deleteRoundById(dummySession, dummyRound.id)
        }
        assertEquals("Player can only delete round on owned table", thrown.message)

        verify(exactly = 1) {
            roundRepository.getRoundOrNull(dummyRound.id)
            tableRepository.getTableByGameIdOrNull(dummyRound.gameId)
        }
    }

    @Test
    fun `deleteRoundById throws if game is already complete`() {
        val dummyRound = Round(UUID.randomUUID(), 1, 157, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        every {
            roundRepository.getRoundOrNull(dummyRound.id)
        } returns dummyRound

        every {
            tableRepository.getTableByGameIdOrNull(dummyRound.gameId)
        } returns createDummyTable(dummyRound, dummySession.playerId, true)

        every {
            roundRepository.deleteRoundById(dummyRound.id)
        } returns true

        val thrown = assertThrows<IllegalArgumentException> {
            service.deleteRoundById(dummySession, dummyRound.id)
        }
        assertEquals("Player cannot delete round of completed game", thrown.message)

        verify(exactly = 1) {
            roundRepository.getRoundOrNull(dummyRound.id)
            tableRepository.getTableByGameIdOrNull(dummyRound.gameId)
        }
    }

    @Test
    fun `updateRound updates score`() {
        val id = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val oldRound = Round(id, 1, 80, gameId, UUID.randomUUID(), UUID.randomUUID())
        val updatedRound = oldRound.copy(score = 95)

        every {
            roundRepository.getRoundOrNull(id)
        } returns oldRound

        every {
            tableRepository.getTableByGameIdOrNull(gameId)
        } returns createDummyTable(oldRound, dummySession.playerId)

        every {
            roundRepository.updateRound(updatedRound)
        } just Runs

        service.updateRound(dummySession, updatedRound)

        verify(exactly = 1) {
            roundRepository.getRoundOrNull(id)
            tableRepository.getTableByGameIdOrNull(gameId)
            roundRepository.updateRound(updatedRound)
        }
    }

    @Test
    fun `updateRound throws if score is below 0`() {
        val id = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val oldRound = Round(id, 1, 80, gameId, UUID.randomUUID(), UUID.randomUUID())
        val updatedRound = oldRound.copy(score = -1)

        val thrown = assertThrows<IllegalArgumentException> {
            service.updateRound(dummySession, updatedRound)
        }
        assertEquals("Score must be between 0..157", thrown.message)
    }

    @Test
    fun `updateRound throws if score is above 157`() {
        val id = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val oldRound = Round(id, 1, 80, gameId, UUID.randomUUID(), UUID.randomUUID())
        val updatedRound = oldRound.copy(score = 158)

        val thrown = assertThrows<IllegalArgumentException> {
            service.updateRound(dummySession, updatedRound)
        }
        assertEquals("Score must be between 0..157", thrown.message)
    }

    @Test
    fun `updateRound throws if table is not owned`() {
        val id = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val oldRound = Round(id, 1, 80, gameId, UUID.randomUUID(), UUID.randomUUID())
        val updatedRound = oldRound.copy(score = 95)

        every {
            roundRepository.getRoundOrNull(id)
        } returns oldRound

        every {
            tableRepository.getTableByGameIdOrNull(gameId)
        } returns createDummyTable(oldRound, UUID.randomUUID())

        every {
            roundRepository.updateRound(updatedRound)
        } just Runs

        val thrown = assertThrows<UnauthorizedException> {
            service.updateRound(dummySession, updatedRound)
        }
        assertEquals("Player can only update round on owned table", thrown.message)

        verify(exactly = 1) {
            roundRepository.getRoundOrNull(id)
            tableRepository.getTableByGameIdOrNull(gameId)
        }
    }

    @Test
    fun `updateRound throws if game is already completed`() {
        val id = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val oldRound = Round(id, 1, 80, gameId, UUID.randomUUID(), UUID.randomUUID())
        val updatedRound = oldRound.copy(score = 95)

        every {
            roundRepository.getRoundOrNull(id)
        } returns oldRound

        every {
            tableRepository.getTableByGameIdOrNull(gameId)
        } returns createDummyTable(oldRound, dummySession.playerId, true)

        every {
            roundRepository.updateRound(updatedRound)
        } just Runs

        val thrown = assertThrows<IllegalArgumentException> {
            service.updateRound(dummySession, updatedRound)
        }
        assertEquals("Player cannot update round of completed game", thrown.message)

        verify(exactly = 1) {
            roundRepository.getRoundOrNull(id)
            tableRepository.getTableByGameIdOrNull(gameId)
        }
    }

    private fun createDummyTable(round: Round, ownerId: UUID, isGameCompleted: Boolean = false) = Table(
        id = UUID.randomUUID(),
        name = "dummy",
        ownerId = ownerId,
        listOf(Game(
            id = round.gameId,
            startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
            endTime = if (isGameCompleted) Clock.System.now().toLocalDateTime(TimeZone.UTC) else null,
            rounds = listOf(round),
            team1 = Team(GameParticipation(ownerId, "T1P1"), GameParticipation(round.playerId, "T1P2")),
            team2 = Team(GameParticipation(UUID.randomUUID(), "T2P1"), GameParticipation(UUID.randomUUID(), "T2P2"))
        ))
    )
}
