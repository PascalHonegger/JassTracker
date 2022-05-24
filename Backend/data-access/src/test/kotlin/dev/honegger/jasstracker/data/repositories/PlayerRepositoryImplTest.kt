package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.util.toUUID
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PlayerRepositoryImplTest : RepositoryTest() {
    private val repo = PlayerRepositoryImpl()

    @Test
    fun `getPlayerOrNull returns correct RegisteredPlayer`() {
        val id = "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID()
        val expected = RegisteredPlayer(
            id,
            "pascal",
            "Pascal",
            "\$argon2i\$v=19\$m=65536,t=10,p=1\$5wpH0v3ZedN9jKSnHL2ZEA\$YzAaj767apGmM1NVn9Kz8gwQg8XcINq5Sc4q8Ho0dyU"
        )
        assertEquals(expected, repo.getPlayerOrNull(id))
    }

    @Test
    fun `getPlayerOrNull returns correct GuestPlayer`() {
        val id = "665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3".toUUID()
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
    fun `updatePlayerDisplayName updates player displayName`() {
        val newPlayer = RegisteredPlayer(
            id = UUID.randomUUID(),
            username = "updatedisplayname_test",
            displayName = "Old DisplayName",
            password = "password",
        )
        repo.savePlayer(newPlayer)
        repo.updatePlayerDisplayName(newPlayer.id, "New")
        val player = repo.getPlayerOrNull(newPlayer.id)
        assertIs<RegisteredPlayer>(player)
        assertEquals("New", player.displayName)
    }

    @Test
    fun `updatePlayer with GuestPlayer makes registered player to guest`() {
        val id = UUID.randomUUID()
        val registered = RegisteredPlayer(
            id = id,
            username = "update_test",
            displayName = "Old",
            password = "pw",
        )
        repo.savePlayer(registered)
        val guest = GuestPlayer(id)
        repo.updatePlayer(guest)
        assertEquals(guest, repo.getPlayerOrNull(id))
    }

    @Test
    fun `getPlayersPerTable returns correct players`() {
        val tableId = "de940c47-9881-4e95-bc3d-6014ad1902e1".toUUID()
        val expected = setOf(
            GuestPlayer("665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3".toUUID()),
            RegisteredPlayer(
                "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
                "pascal",
                "Pascal",
                "\$argon2i\$v=19\$m=65536,t=10,p=1\$5wpH0v3ZedN9jKSnHL2ZEA\$YzAaj767apGmM1NVn9Kz8gwQg8XcINq5Sc4q8Ho0dyU"
            ),
            GuestPlayer("7dad81d3-62db-4553-9d48-f38f404f1596".toUUID()),
            RegisteredPlayer(
                "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(),
                "marcel",
                "Marcel",
                "\$argon2i\$v=19\$m=65536,t=10,p=1\$k+zwEFts6FNMRf/LLKU2mw\$kq4McVUA9mOUcbmWg4sQeQv98/m5d5b37QEPIoBxQrQ"
            )
        )
        assertEquals(expected, repo.getPlayersPerTable(tableId).toSet())
    }
}
