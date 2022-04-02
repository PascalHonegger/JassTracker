package dev.honegger.services

import dev.honegger.domain.Game
import dev.honegger.domain.UserSession
import dev.honegger.repositories.GameRepository
import io.mockk.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.*
import kotlin.test.*

class GameServiceImplTest {
    private val repository = mockk<GameRepository>()
    private val dummySession = UserSession(UUID.randomUUID(), "dummy")
    private val passedGame = slot<Game>()
    private val passedTableId = slot<UUID>()
    private val dummyNow = LocalDateTime(2022, 4, 2, 13, 58, 0)
    private val dummyClock = mockk<Clock> { every { now() } returns dummyNow.toInstant(TimeZone.UTC) }
    private val service = GameServiceImpl(repository, dummyClock)

    @BeforeTest
    fun setup() {
        clearMocks(repository)
        passedGame.clear()
        every { repository.saveGame(capture(passedGame), capture(passedTableId)) } just Runs
    }

    @AfterTest
    fun teardown() {
        confirmVerified(repository)
    }

    @Test
    fun `createGame creates scoreboard in repository`() {
        val dummyTableId = UUID.randomUUID()
        val created = service.createGame(dummySession, dummyTableId)

        assertTrue { passedGame.isCaptured }
        assertTrue { passedTableId.isCaptured }
        assertEquals(created, passedGame.captured)
        assertEquals(dummyNow, created.startTime)
        assertEquals(null, created.endTime)
        assertEquals(dummyTableId, passedTableId.captured)
        verify(exactly = 1) { repository.saveGame(any(), any()) }
    }
}
