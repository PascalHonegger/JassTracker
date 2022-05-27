package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.exceptions.NotFoundException
import dev.honegger.jasstracker.domain.exceptions.UnauthorizedException
import dev.honegger.jasstracker.domain.repositories.GameRepository
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import io.mockk.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.*

class GameServiceImplTest {
    private val gameRepository = mockk<GameRepository>()
    private val tableRepository = mockk<TableRepository>()
    private val playerRepository = mockk<PlayerRepository>()
    private val dummySession = PlayerSession(UUID.randomUUID(), false, "dummy", "Dummy")
    private val passedGame = slot<Game>()
    private val passedPlayers = mutableListOf<Player>()
    private val passedTableId = slot<UUID>()
    private val dummyNow = LocalDateTime(2022, 4, 2, 13, 58, 0)
    private val dummyClock = mockk<Clock> { every { now() } returns dummyNow.toInstant(TimeZone.UTC) }
    private val service = GameServiceImpl(gameRepository, tableRepository, playerRepository, dummyClock)

    @BeforeTest
    fun setup() {
        clearMocks(gameRepository, tableRepository, playerRepository)
        passedGame.clear()
        every { gameRepository.saveGame(capture(passedGame), capture(passedTableId)) } just Runs
        every { playerRepository.savePlayer(capture(passedPlayers)) } just Runs
    }

    @AfterTest
    fun teardown() {
        confirmVerified(gameRepository, tableRepository, playerRepository)
    }

    @Test
    fun `createGame creates game and guest players in repository`() {
        val dummyTableId = UUID.randomUUID()
        val team1Player1 = CreateGameParticipation(UUID.randomUUID(), "T1P1")
        val team1Player2 = CreateGameParticipation(null, "T1P2")
        val team2Player1 = CreateGameParticipation(null, "T2P1")
        val team2Player2 = CreateGameParticipation(null, "T2P2")
        val created = service.createGame(
            dummySession, dummyTableId,
            team1Player1,
            team1Player2,
            team2Player1,
            team2Player2,
        )

        val (createdPlayer1, createdPlayer2, createdPlayer3) = passedPlayers

        assertTrue { passedGame.isCaptured }
        assertTrue { passedTableId.isCaptured }
        assertEquals(created, passedGame.captured)
        assertEquals(dummyNow, created.startTime)
        assertEquals(null, created.endTime)
        assertEquals(emptyList(), created.rounds)
        assertEquals(GameParticipation(team1Player1.playerId!!, "T1P1"), created.team1.player1)
        assertEquals(GameParticipation(createdPlayer1.id, "T1P2"), created.team1.player2)
        assertEquals(GameParticipation(createdPlayer2.id, "T2P1"), created.team2.player1)
        assertEquals(GameParticipation(createdPlayer3.id, "T2P2"), created.team2.player2)
        assertEquals(dummyTableId, passedTableId.captured)
        verify(exactly = 1) {
            gameRepository.saveGame(any(), any())
            playerRepository.savePlayer(createdPlayer1)
            playerRepository.savePlayer(createdPlayer2)
            playerRepository.savePlayer(createdPlayer3)
        }
    }

    @Test
    fun `deleteGameById deletes game in repository`() {
        val dummyGame = createDummyGame()
        val dummyGameId = dummyGame.id
        every { tableRepository.getTableByGameIdOrNull(dummyGameId) } returns createDummyTable(dummySession.playerId, dummyGame)
        every { gameRepository.deleteGameById(dummyGameId) } returns true
        service.deleteGameById(dummySession, dummyGameId)
        verify(exactly = 1) {
            tableRepository.getTableByGameIdOrNull(dummyGameId)
            gameRepository.deleteGameById(dummyGameId)
        }
    }

    @Test
    fun `getGameOrNull returns game from repository`() {
        val dummyGame = createDummyGame()
        val dummyGameId = dummyGame.id
        every { gameRepository.getGameOrNull(dummyGameId) } returns dummyGame
        assertEquals(dummyGame, service.getGameOrNull(dummySession, dummyGameId))
        verify(exactly = 1) {
            gameRepository.getGameOrNull(dummyGameId)
        }
    }

    @Test
    fun `updateGame updates endTime`() {
        val existingGame = createDummyGame()
        val gameId = existingGame.id
        val updatedGame = existingGame.let {
            it.copy(endTime = it.startTime.toInstant(TimeZone.UTC).plus(1, DateTimeUnit.HOUR).toLocalDateTime(TimeZone.UTC))
        }
        every { gameRepository.getGameOrNull(gameId) } returns existingGame
        every { tableRepository.getTableByGameIdOrNull(gameId) } returns createDummyTable(dummySession.playerId, existingGame)
        every { gameRepository.updateGame(updatedGame) } just Runs
        service.updateGame(dummySession, updatedGame)
        verify(exactly = 1) {
            gameRepository.getGameOrNull(gameId)
            tableRepository.getTableByGameIdOrNull(gameId)
            gameRepository.updateGame(updatedGame)
        }
    }

    @Test
    fun `update of unowned game throws UnauthorizedException`() {
        val game = createDummyGame()
        val gameId = game.id
        every { gameRepository.getGameOrNull(gameId) } returns game
        every { tableRepository.getTableByGameIdOrNull(gameId) } returns createDummyTable(UUID.randomUUID(), game)
        assertThrows<UnauthorizedException> { service.updateGame(dummySession, game) }
        verify(exactly = 1) {
            gameRepository.getGameOrNull(gameId)
            tableRepository.getTableByGameIdOrNull(gameId)
        }
    }

    @Test
    fun `update of non existent game throws NotFoundException`() {
        val game = createDummyGame()
        val gameId = game.id
        every { gameRepository.getGameOrNull(gameId) } returns null
        assertThrows<NotFoundException> { service.updateGame(dummySession, game) }
        verify(exactly = 1) {
            gameRepository.getGameOrNull(gameId)
        }
    }

    @Test
    fun `delete of unowned game throws UnauthorizedException`() {
        val game = createDummyGame()
        val gameId = game.id
        every { gameRepository.getGameOrNull(gameId) } returns game
        every { tableRepository.getTableByGameIdOrNull(gameId) } returns createDummyTable(UUID.randomUUID(), game)
        assertThrows<UnauthorizedException> { service.deleteGameById(dummySession, gameId) }
        verify(exactly = 1) {
            tableRepository.getTableByGameIdOrNull(gameId)
        }
    }

    @Test
    fun `delete of non existent game throws NotFoundException`() {
        val gameId = UUID.randomUUID()
        every { tableRepository.getTableByGameIdOrNull(gameId) } returns null
        assertThrows<NotFoundException> { service.deleteGameById(dummySession, gameId) }
        verify(exactly = 1) {
            tableRepository.getTableByGameIdOrNull(gameId)
        }
    }

    private fun createDummyGame() = Game(
        UUID.randomUUID(),
        LocalDateTime(2022, 4, 20, 13, 37),
        null,
        emptyList(),
        Team(GameParticipation(UUID.randomUUID(), "T1P1"), GameParticipation(UUID.randomUUID(), "T1P2")),
        Team(GameParticipation(UUID.randomUUID(), "T2P1"), GameParticipation(UUID.randomUUID(), "T2P2"))
    )

    private fun createDummyTable(ownerId: UUID, game: Game) = Table(
        UUID.randomUUID(),
        "Dummy Table",
        ownerId,
        listOf(game)
    )
}
