package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipant
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.UserSession
import dev.honegger.jasstracker.domain.repositories.GameRepository
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import io.mockk.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.*
import kotlin.test.*

class GameServiceImplTest {
    private val gameRepository = mockk<GameRepository>()
    private val playerRepository = mockk<PlayerRepository>()
    private val dummySession = UserSession(UUID.randomUUID(), "dummy")
    private val passedGame = slot<Game>()
    private val passedPlayers = mutableListOf<Player>()
    private val passedTableId = slot<UUID>()
    private val dummyNow = LocalDateTime(2022, 4, 2, 13, 58, 0)
    private val dummyClock = mockk<Clock> { every { now() } returns dummyNow.toInstant(TimeZone.UTC) }
    private val service = GameServiceImpl(gameRepository, playerRepository, dummyClock)

    @BeforeTest
    fun setup() {
        clearMocks(gameRepository, playerRepository)
        passedGame.clear()
        every { gameRepository.saveGame(capture(passedGame), capture(passedTableId)) } just Runs
        every { playerRepository.savePlayer(capture(passedPlayers)) } just Runs
    }

    @AfterTest
    fun teardown() {
        confirmVerified(gameRepository, playerRepository)
    }

    @Test
    fun `createGame creates game and guest players in repository`() {
        val dummyTableId = UUID.randomUUID()
        val team1Player1 = CreateGameParticipant(UUID.randomUUID(), "T1P1")
        val team1Player2 = CreateGameParticipant(null, "T1P2")
        val team2Player1 = CreateGameParticipant(null, "T2P1")
        val team2Player2 = CreateGameParticipant(null, "T2P2")
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
        assertEquals(GameParticipant(team1Player1.playerId!!, "T1P1"), created.team1.player1)
        assertEquals(GameParticipant(createdPlayer1.id, "T1P2"), created.team1.player2)
        assertEquals(GameParticipant(createdPlayer2.id, "T2P1"), created.team2.player1)
        assertEquals(GameParticipant(createdPlayer3.id, "T2P2"), created.team2.player2)
        assertEquals(dummyTableId, passedTableId.captured)
        verify(exactly = 1) {
            gameRepository.saveGame(any(), any())
            playerRepository.savePlayer(createdPlayer1)
            playerRepository.savePlayer(createdPlayer2)
            playerRepository.savePlayer(createdPlayer3)
        }
    }
}
