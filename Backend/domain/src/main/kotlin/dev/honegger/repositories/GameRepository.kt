package dev.honegger.repositories

import dev.honegger.domain.Game
import java.util.UUID

interface GameRepository {
    fun getAllGames(): List<Game>
    fun getGameOrNull(id: UUID): Game?
    fun updateGame(updatedGame: Game)
    fun saveGame(newGame: Game, tableId: UUID)
}
