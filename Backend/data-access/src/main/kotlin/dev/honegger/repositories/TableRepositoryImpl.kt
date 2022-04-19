package dev.honegger.repositories

import dev.honegger.domain.Table
import dev.honegger.jasstracker.database.tables.Game.GAME
import dev.honegger.jasstracker.database.tables.Table.TABLE
import dev.honegger.withContext
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
            val tableRecord = selectFrom(TABLE).where(TABLE.ID.eq(game.id)).fetchOne()

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
        selectFrom(TABLE).where(TABLE.OWNER_ID.eq(ownerId)).fetch().map {
            Table(
                id = it.id,
                name = it.name,
                ownerId = it.ownerId,
                games = gameRepository.getAllGamesOfTable(it.id),
            )
        }
    }

    override fun updateTable(updatedTable: Table): Unit = withContext {
        val tableRecord = selectFrom(TABLE).where(TABLE.ID.eq(updatedTable.id)).fetchOne()
        checkNotNull(tableRecord)

        tableRecord.name = tableRecord.name
        tableRecord.ownerId = tableRecord.ownerId
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
}
