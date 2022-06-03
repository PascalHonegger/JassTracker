package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.data.database.Keys
import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipation
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.data.database.tables.GameParticipation.GAME_PARTICIPATION as GP
import dev.honegger.jasstracker.data.database.tables.Game.GAME
import dev.honegger.jasstracker.data.database.tables.Round.ROUND
import dev.honegger.jasstracker.data.database.tables.records.GameParticipationRecord
import dev.honegger.jasstracker.data.database.tables.records.GameRecord
import dev.honegger.jasstracker.domain.repositories.GameRepository
import dev.honegger.jasstracker.data.withContext
import org.jooq.DSLContext
import org.jooq.Result
import java.util.*

class GameRepositoryImpl : GameRepository {

    override fun getAllGamesOfTable(tableId: UUID): List<Game> = withContext {
        toDomainObjects(
            selectFrom(GAME)
                .where(GAME.TABLE_ID.eq(tableId))
                .fetch()
        )
    }

    override fun getGroupedGamesOfTables(tableIds: List<UUID>): Map<UUID, List<Game>> = withContext {
        val records = selectFrom(GAME).where(GAME.TABLE_ID.`in`(tableIds)).fetch()
        val lookup = records.intoMap(GAME.ID)
        val games = toDomainObjects(records)
        games.groupBy { lookup.getValue(it.id).tableId }.withDefault { emptyList() }
    }

    override fun getGameOrNull(id: UUID): Game? = withContext {
        toDomainObjects(selectFrom(GAME).where(GAME.ID.eq(id)).fetch()).singleOrNull()
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

        fun GameParticipation.toGameParticipation(tablePosition: Int) = newRecord(GP).apply {
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

    private fun DSLContext.toDomainObjects(records: Result<GameRecord>): List<Game> {
        if (records.isEmpty()) {
            return emptyList()
        }
        val gameIds = records.map { it.id }
        val participations = records.fetchChildren(Keys.GAME_PARTICIPATION__GAME_PARTICIPATION_GAME_ID_FKEY).intoGroups(GP.GAME_ID)
        val rounds = selectFrom(ROUND).where(ROUND.GAME_ID.`in`(gameIds)).fetchGroups(ROUND.GAME_ID, Round::class.java)
        return records.map { convertToDomainGame(it, participations[it.id] ?: emptyList(), rounds[it.id] ?: emptyList()) }
    }

    private fun convertToDomainGame(record: GameRecord, participations: List<GameParticipationRecord>, rounds: List<Round>): Game {
        check(participations.size == 4) { "Expected exactly 4 game participations for game ${record.id} but found ${participations.size}" }
        val team1Player1 = participations.single { it.tablePosition == 0 }.toGameParticipation()
        val team1Player2 = participations.single { it.tablePosition == 1 }.toGameParticipation()
        val team2Player1 = participations.single { it.tablePosition == 2 }.toGameParticipation()
        val team2Player2 = participations.single { it.tablePosition == 3 }.toGameParticipation()
        return Game(
            record.id,
            record.startTime,
            record.endTime,
            rounds,
            Team(team1Player1, team1Player2),
            Team(team2Player1, team2Player2)
        )
    }

    private fun GameParticipationRecord.toGameParticipation() = GameParticipation(playerId, playerName)
}
