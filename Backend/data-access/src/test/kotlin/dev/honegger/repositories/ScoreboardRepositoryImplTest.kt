package dev.honegger.repositories

import dev.honegger.domain.Scoreboard
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ScoreboardRepositoryImplTest {
    @Test
    fun `getScoreboard returns correct scoreboard after saveScoreboard is called`() {
        val repository = ScoreboardRepositoryImpl()
        val newBoard = Scoreboard(
            id = "Some ID",
            name = "Some Name",
            ownerId = "Some owner",
        )
        assertNull(repository.getScoreboardOrNull(newBoard.id))
        repository.saveScoreboard(newBoard)
        assertEquals(newBoard, repository.getScoreboardOrNull(newBoard.id))
    }
}
