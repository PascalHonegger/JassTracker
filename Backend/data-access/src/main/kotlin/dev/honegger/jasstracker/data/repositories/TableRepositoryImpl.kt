package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.Table
import dev.honegger.jasstracker.data.database.tables.Game.GAME
import dev.honegger.jasstracker.data.database.tables.Table.TABLE
import dev.honegger.jasstracker.data.database.tables.GameParticipation.GAME_PARTICIPATION as GP
import dev.honegger.jasstracker.domain.repositories.GameRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import dev.honegger.jasstracker.data.withContext
import java.util.*

class TableRepositoryImpl(private val gameRepository: GameRepository) : TableRepository {
    override fun getTableOrNull(id: UUID): Table? = withContext {
        val tableRecord = selectFrom(TABLE).where(TABLE.ID.eq(id)).fetchOne()

        tableRecord?.let {
            Table(
                id = it.id,
                name = it.name,
                ownerId = it.ownerId,
                games = gameRepository.getAllGamesOfTable(it.id),
            )
        }
    }

    override fun getTableByGameIdOrNull(id: UUID): Table? = withContext {
        selectFrom(GAME).where(GAME.ID.eq(id)).fetchOne()?.let { game ->
            val tableRecord = selectFrom(TABLE).where(TABLE.ID.eq(game.tableId)).fetchOne()

            tableRecord?.let {
                Table(
                    id = it.id,
                    name = it.name,
                    ownerId = it.ownerId,
                    games = listOf(gameRepository.getGameOrNull(game.id)!!),
                )
            }
        }
    }

    override fun getTables(ownerId: UUID): List<Table> = withContext {
        val records = selectFrom(TABLE).where(TABLE.OWNER_ID.eq(ownerId)).fetch()
        val groupedGames = gameRepository.getGroupedGamesOfTables(records.map { it.id })

        records.map {
            Table(
                id = it.id,
                name = it.name,
                ownerId = it.ownerId,
                games = groupedGames[it.id] ?: emptyList(),
            )
        }
    }

    override fun updateTable(updatedTable: Table): Unit = withContext {
        val tableRecord = selectFrom(TABLE).where(TABLE.ID.eq(updatedTable.id)).fetchOne()
        checkNotNull(tableRecord)

        tableRecord.name = updatedTable.name
        tableRecord.ownerId = updatedTable.ownerId
        tableRecord.update()
    }

    override fun saveTable(newTable: Table): Unit = withContext {
        val newRecord = newRecord(TABLE).apply {
            this.id = newTable.id
            this.name = newTable.name
            this.ownerId = newTable.ownerId
        }
        newRecord.insert()
    }

    override fun deleteTableById(id: UUID): Boolean = withContext {
        val gameIds = select(GAME.ID).from(GAME).where(GAME.TABLE_ID.eq(id)).fetch()
        deleteFrom(GP).where(GP.GAME_ID.`in`(gameIds)).execute()
        deleteFrom(GAME).where(GAME.TABLE_ID.eq(id)).execute()
        return@withContext deleteFrom(TABLE).where(TABLE.ID.eq(id)).execute() == 1
    }
}
