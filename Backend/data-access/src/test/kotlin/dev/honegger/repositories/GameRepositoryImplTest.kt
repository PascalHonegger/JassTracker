package dev.honegger.repositories

import dev.honegger.domain.Game
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Disabled
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GameRepositoryImplTest {
    @Test
    @Disabled
    fun `saveGame saved new game to database`() {
        val repository = GameRepositoryImpl()
        val newBoard = Game(
            id = UUID.randomUUID(),
            startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
            rounds = emptyList()
        )
        assertNull(repository.getGameOrNull(newBoard.id))
        repository.saveGame(newBoard, UUID.randomUUID())
        assertEquals(newBoard, repository.getGameOrNull(newBoard.id))
    }
}
