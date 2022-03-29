package dev.honegger.repositories

import dev.honegger.domain.Game
import dev.honegger.jasstracker.database.tables.Game.GAME
import dev.honegger.withContext
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.*

class GameRepositoryImpl : GameRepository {
    override fun getAllGames(): List<Game> = withContext {
        selectFrom(GAME).fetch().map {
            Game(
                id = it.id,
                startTime = it.startTime.toKotlinLocalDateTime(),
                endTime = it.endTime?.toKotlinLocalDateTime()
            )
        }
    }

    override fun getGameOrNull(id: UUID): Game? = withContext {
        val gameRecord = selectFrom(GAME).where(GAME.ID.eq(id)).fetchOne()

        gameRecord?.let {
            Game(
                id = it.id,
                startTime = it.startTime.toKotlinLocalDateTime(),
                endTime = it.endTime?.toKotlinLocalDateTime()
            )
        }
    }

    override fun updateGame(updatedGame: Game): Unit = withContext {
        val gameRecord = selectFrom(GAME).where(GAME.ID.eq(updatedGame.id)).fetchOne()
        checkNotNull(gameRecord)

        gameRecord.startTime = updatedGame.startTime.toJavaLocalDateTime()
        gameRecord.endTime = updatedGame.endTime?.toJavaLocalDateTime()
        gameRecord.store()
    }

    override fun saveGame(newGame: Game, tableId: UUID): Unit = withContext {
        val newRecord = newRecord(GAME).apply {
            this.id = newGame.id
            this.startTime = newGame.startTime.toJavaLocalDateTime()
            this.endTime = newGame.endTime?.toJavaLocalDateTime()
            this.tableId = tableId
        }
        newRecord.store()
    }
}
