package dev.honegger.repositories

import dev.honegger.domain.Player
import java.util.UUID

interface PlayerRepository {
    fun getPlayersPerTable(tableId: UUID): List<Player>
    fun getPlayerOrNull(id: UUID): Player?
    fun updatePlayer(updatedPlayer: Player)
    fun savePlayer(newPlayer: Player)
}
