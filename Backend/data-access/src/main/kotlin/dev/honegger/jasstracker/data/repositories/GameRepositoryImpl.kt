package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipant
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.data.database.tables.GameParticipation.GAME_PARTICIPATION as GP
import dev.honegger.jasstracker.data.database.tables.Game.GAME
import dev.honegger.jasstracker.data.database.tables.Round.ROUND
import dev.honegger.jasstracker.data.database.tables.records.GameRecord
import dev.honegger.jasstracker.domain.repositories.GameRepository
import dev.honegger.withContext
import org.jooq.DSLContext
import java.util.*

class GameRepositoryImpl : GameRepository {
    private fun DSLContext.toGame(record: GameRecord): Game {
        val gameParticipations = selectFrom(GP).where(GP.GAME_ID.eq(record.id)).fetch()
        check(gameParticipations.size == 4) { "Expected exactly 4 game participations for game ${record.id} but found ${gameParticipations.size}" }
        val team1Player1 = gameParticipations.single { it.tablePosition == 0 }
        val team1Player2 = gameParticipations.single { it.tablePosition == 1 }
        val team2Player1 = gameParticipations.single { it.tablePosition == 2 }
        val team2Player2 = gameParticipations.single { it.tablePosition == 3 }
        return Game(
            id = record.id,
            startTime = record.startTime,
            endTime = record.endTime,
            rounds = selectFrom(ROUND).where(ROUND.GAME_ID.eq(record.id)).fetch().map { round ->
                Round(
                    round.id,
                    round.number,
                    round.score,
                    round.gameId,
                    round.playerId,
                    round.contractId
                )
            },
            team1 = Team(
                player1 = GameParticipant(team1Player1.playerId, team1Player1.playerName),
                player2 = GameParticipant(team1Player2.playerId, team1Player2.playerName),
            ),
            team2 = Team(
                player1 = GameParticipant(team2Player1.playerId, team2Player1.playerName),
                player2 = GameParticipant(team2Player2.playerId, team2Player2.playerName),
            ),
        )
    }

    override fun getAllGames(): List<Game> = withContext {
        selectFrom(GAME).fetch().map {
            toGame(it)
        }
    }

    override fun getAllGamesOfTable(tableId: UUID): List<Game> = withContext {
        selectFrom(GAME).where(GAME.TABLE_ID.eq(tableId)).fetch().map {
            toGame(it)
        }
    }

    override fun getGameOrNull(id: UUID): Game? = withContext {
        val gameRecord = selectFrom(GAME).where(GAME.ID.eq(id)).fetchOne()

        gameRecord?.let { toGame(it) }
    }

    override fun updateGame(updatedGame: Game): Unit = withContext {
        val gameRecord = selectFrom(GAME).where(GAME.ID.eq(updatedGame.id)).fetchOne()
        checkNotNull(gameRecord)

        gameRecord.startTime = updatedGame.startTime
        gameRecord.endTime = updatedGame.endTime
        gameRecord.update()
    }

    override fun saveGame(newGame: Game, tableId: UUID): Unit = withContext {
        val newGameRecord = newRecord(GAME).apply {
            this.id = newGame.id
            this.startTime = newGame.startTime
            this.endTime = newGame.endTime
            this.tableId = tableId
        }
        newGameRecord.insert()

        fun GameParticipant.toGameParticipation(tablePosition: Int) = newRecord(GP).apply {
            this.gameId = newGameRecord.id
            this.playerId = this@toGameParticipation.playerId
            this.playerName = this@toGameParticipation.displayName
            this.tablePosition = tablePosition
        }

        newGame.team1.player1.toGameParticipation(tablePosition = 0).insert()
        newGame.team1.player2.toGameParticipation(tablePosition = 1).insert()
        newGame.team2.player1.toGameParticipation(tablePosition = 2).insert()
        newGame.team2.player2.toGameParticipation(tablePosition = 3).insert()
    }

    override fun deleteGameById(id: UUID): Boolean = withContext {
        deleteFrom(GP).where(GP.GAME_ID.eq(id)).execute()
        return@withContext deleteFrom(GAME).where(GAME.ID.eq(id)).execute() == 1
    }
}
