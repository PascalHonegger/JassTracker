package dev.honegger.jasstracker.domain.repositories

import dev.honegger.jasstracker.domain.Game
import java.util.UUID

interface GameRepository {
    fun getAllGames(): List<Game>
    fun getAllGamesOfTable(tableId: UUID): List<Game>
    fun getGroupedGamesOfTables(tableIds: List<UUID>): Map<UUID, List<Game>>
    fun getGameOrNull(id: UUID): Game?
    fun updateGame(updatedGame: Game)
    fun saveGame(newGame: Game, tableId: UUID)
    fun deleteGameById(id: UUID): Boolean
}
