package dev.honegger.jasstracker.domain.repositories

import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.RegisteredPlayer
import java.util.UUID

interface PlayerRepository {
    fun getPlayersPerTable(tableId: UUID): List<Player>
    fun getPlayerOrNull(id: UUID): Player?
    fun updatePlayer(updatedPlayer: RegisteredPlayer)
    fun savePlayer(newPlayer: Player)
    fun findPlayerByUsername(username: String): RegisteredPlayer?
}
