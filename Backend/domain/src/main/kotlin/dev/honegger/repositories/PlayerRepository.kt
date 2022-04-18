package dev.honegger.repositories

import dev.honegger.domain.Player
import dev.honegger.domain.RegisteredPlayer
import java.util.UUID

interface PlayerRepository {
    fun getPlayersPerTable(tableId: UUID): List<Player>
    fun getPlayerOrNull(id: UUID): Player?
    fun updatePlayer(updatedPlayer: RegisteredPlayer)
    fun savePlayer(newPlayer: Player)
}
