package dev.honegger.repositories

import dev.honegger.domain.Round
import org.junit.jupiter.api.Disabled
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RoundRepositoryImplTest {
    @Test
    @Disabled
    fun `getRound returns correct round after saveRound is called`() {
        val repository = RoundRepositoryImpl()
        val newRound = Round(
            id = UUID.randomUUID(),
            number = 1,
            score = 150,
            gameId = UUID.randomUUID(),
            playerId = UUID.randomUUID(),
            contractId = UUID.randomUUID(),
        )
        assertNull(repository.getRoundOrNull(newRound.id))
        repository.saveRound(newRound)
        assertEquals(newRound, repository.getRoundOrNull(newRound.id))
    }

    @Test
    @Disabled
    fun `getRounds returns multiple rounds after saveRound is called`() {
        val repository = RoundRepositoryImpl()
        val gameId = UUID.randomUUID()
        val round1 = Round(
            id = UUID.randomUUID(),
            number = 1,
            score = 150,
            gameId = gameId,
            playerId = UUID.randomUUID(),
            contractId = UUID.randomUUID(),
        )
        val round2 = Round(
            id = UUID.randomUUID(),
            number = 2,
            score = 140,
            gameId = gameId,
            playerId = UUID.randomUUID(),
            contractId = UUID.randomUUID(),
        )
        assertEquals(emptyList(), repository.getRoundsForGame(gameId))
        repository.saveRound(round1)
        repository.saveRound(round2)
        assertEquals(listOf(round1, round2), repository.getRoundsForGame(gameId))
    }
}
