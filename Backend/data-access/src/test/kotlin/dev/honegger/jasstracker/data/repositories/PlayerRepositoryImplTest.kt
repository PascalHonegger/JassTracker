package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.RegisteredPlayer
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerRepositoryImplTest : RepositoryTest() {
    private val repo = PlayerRepositoryImpl()

    @Test
    fun `getPlayerOrNull returns correct RegisteredPlayer`() {
        val id = UUID.fromString("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0")
        val expected = RegisteredPlayer(
            id,
            "pascal",
            "Pascal",
            "honegger"
        )
        assertEquals(expected, repo.getPlayerOrNull(id))
    }

    @Test
    fun `getPlayerOrNull returns correct GuestPlayer`() {
        val id = UUID.fromString("665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3")
        assertEquals(GuestPlayer(id), repo.getPlayerOrNull(id))
    }

    @Test
    fun `updatePlayer updates player`() {
        val id = UUID.randomUUID()
        val newPlayer = RegisteredPlayer(
            id = id,
            username = "update_test",
            displayName = "Old",
            password = "pw",
        )
        repo.savePlayer(newPlayer)
        val updatedPlayer = newPlayer.copy(displayName = "New")
        repo.updatePlayer(updatedPlayer)
        assertEquals(updatedPlayer, repo.getPlayerOrNull(id))
    }

    @Test
    fun `getPlayersPerTable returns correct players`() {
        val tableId = UUID.fromString("de940c47-9881-4e95-bc3d-6014ad1902e1")
        val expected = setOf(
            GuestPlayer(UUID.fromString("665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3")),
            RegisteredPlayer(
                UUID.fromString("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0"),
                "pascal",
                "Pascal",
                "honegger"
            ),
            GuestPlayer(UUID.fromString("7dad81d3-62db-4553-9d48-f38f404f1596")),
            RegisteredPlayer(
                UUID.fromString("3095c042-d0a9-4219-9f65-53d4565fd1e6"),
                "marcel",
                "Marcel",
                "joss"
            )
        )
        assertEquals(expected, repo.getPlayersPerTable(tableId).toSet())
    }
}
