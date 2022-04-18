package dev.honegger.repositories

import org.junit.jupiter.api.Disabled
import kotlin.test.Test
import kotlin.test.assertEquals

class GameRepositoryImplTest {
    @Test
    @Disabled
    fun `getAllGames returns empty list for empty database`() {
        val repository = GameRepositoryImpl()
        assertEquals(emptyList(), repository.getAllGames())
    }
}
