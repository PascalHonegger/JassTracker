package dev.honegger.repositories

import dev.honegger.domain.Game
import dev.honegger.domain.Round
import dev.honegger.jasstracker.database.tables.Game.GAME
import dev.honegger.jasstracker.database.tables.Round.ROUND
import dev.honegger.withContext
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.*

class GameRepositoryImpl : GameRepository {
    override fun getAllGames(): List<Game> = withContext {
        selectFrom(GAME).fetch().map {
            Game(
                id = it.id,
                startTime = it.startTime,
                endTime = it.endTime,
                rounds = selectFrom(ROUND).where(ROUND.GAME_ID.eq(it.id)).fetch().map { round ->
                    Round(
                        round.id,
                        round.number,
                        round.score,
                        round.gameId,
                        round.playerId,
                        round.contractId
                    )
                }
            )
        }
    }

    override fun getGameOrNull(id: UUID): Game? = withContext {
        val gameRecord = selectFrom(GAME).where(GAME.ID.eq(id)).fetchOne()

        gameRecord?.let {
            Game(
                id = it.id,
                startTime = it.startTime,
                endTime = it.endTime,
                rounds = selectFrom(ROUND).where(ROUND.GAME_ID.eq(it.id)).fetch().map { round ->
                    Round(
                        round.id,
                        round.number,
                        round.score,
                        round.gameId,
                        round.playerId,
                        round.contractId
                    )
                },
            )
        }
    }

    override fun updateGame(updatedGame: Game): Unit = withContext {
        val gameRecord = selectFrom(GAME).where(GAME.ID.eq(updatedGame.id)).fetchOne()
        checkNotNull(gameRecord)

        gameRecord.startTime = updatedGame.startTime
        gameRecord.endTime = updatedGame.endTime
        gameRecord.store()
    }

    override fun saveGame(newGame: Game, tableId: UUID): Unit = withContext {
        val newRecord = newRecord(GAME).apply {
            this.id = newGame.id
            this.startTime = newGame.startTime
            this.endTime = newGame.endTime
            this.tableId = tableId
        }
        newRecord.store()
    }
}
