package dev.honegger.repositories

import dev.honegger.domain.RegisteredPlayer
import org.junit.jupiter.api.Disabled
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlayerRepositoryImplTest {
    @Test
    @Disabled
    fun `savePlayer saved new player to database`() {
        val repository = PlayerRepositoryImpl()
        val newPlayer = RegisteredPlayer(
            id = UUID.randomUUID(),
            username = "dummy",
            displayName = "dummy",
            password = "pw",
        )
        assertNull(repository.getPlayerOrNull(newPlayer.id))
        repository.savePlayer(newPlayer)
        assertEquals(newPlayer, repository.getPlayerOrNull(newPlayer.id))
    }
}
