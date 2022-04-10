package dev.honegger.services

import dev.honegger.domain.Round
import dev.honegger.domain.UserSession
import dev.honegger.repositories.RoundRepository
import io.mockk.*
import java.util.*
import kotlin.test.*

class RoundServiceImplTest {
    private val repository = mockk<RoundRepository>()
    private val service = RoundServiceImpl(repository)
    private val dummySession = UserSession(UUID.randomUUID(), "dummy")
    private val passedRound = slot<Round>()

    @BeforeTest
    fun setup() {
        clearMocks(repository)
        passedRound.clear()
        every { repository.saveRound(capture(passedRound)) } just Runs
    }

    @AfterTest
    fun teardown() {
        confirmVerified(repository)
    }

    @Test
    fun `createRound creates round in repository`() {
        val dummyNum = 1
        val dummyScore = 150
        val dummyGame = UUID.randomUUID()
        val dummyPlayer = UUID.randomUUID()
        val dummyContract = UUID.randomUUID()

        val created = service.createRound(dummySession, dummyNum, dummyScore, dummyGame, dummyPlayer, dummyContract)

        assertTrue { passedRound.isCaptured }
        assertEquals(created, passedRound.captured)
        assertEquals(dummyNum, created.number)
        assertEquals(dummyScore, created.score)
        assertEquals(dummyGame, created.gameId)
        assertEquals(dummyPlayer, created.playerId)
        assertEquals(dummyContract, created.contractId)

        verify(exactly = 1) { repository.saveRound(any()) }
    }
}
