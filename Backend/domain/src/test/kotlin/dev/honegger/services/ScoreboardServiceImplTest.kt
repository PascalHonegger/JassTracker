package dev.honegger.services

import dev.honegger.domain.Scoreboard
import dev.honegger.domain.UserSession
import dev.honegger.repositories.ScoreboardRepository
import io.mockk.*
import kotlin.test.*

class ScoreboardServiceImplTest {
    private val repository = mockk<ScoreboardRepository>()
    private val service = ScoreboardServiceImpl(repository)
    private val dummySession = UserSession("dummy", "dummy")
    private val passedScoreboard = slot<Scoreboard>()

    @BeforeTest
    fun setup() {
        clearMocks(repository)
        passedScoreboard.clear()
        every { repository.saveScoreboard(capture(passedScoreboard)) } just Runs
    }

    @AfterTest
    fun teardown() {
        confirmVerified(repository)
    }

    @Test
    fun `createScoreboard creates scoreboard in repository`() {
        val dummyName = "Some Scoreboard"
        val created = service.createScoreboard(dummySession, dummyName)

        assertTrue { passedScoreboard.isCaptured }
        assertEquals(created, passedScoreboard.captured)
        assertTrue { created.id.isNotBlank() }
        assertEquals(dummyName, created.name)
        assertEquals(dummySession.userId, created.ownerId)
        verify(exactly = 1) { repository.saveScoreboard(any()) }
    }
}
