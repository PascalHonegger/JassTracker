package dev.honegger.repositories

import dev.honegger.domain.Game
import dev.honegger.domain.Table
import dev.honegger.jasstracker.database.tables.Table.TABLE
import dev.honegger.jasstracker.database.tables.Game.GAME
import dev.honegger.withContext
import java.util.*

class TableRepositoryImpl : TableRepository {
    override fun getTableOrNull(id: UUID): Table? = withContext {

        val tableRecord = selectFrom(TABLE).where(TABLE.ID.eq(id)).fetchOne()

        tableRecord?.let {
            Table(
                id = it.id,
                name = it.name,
                ownerId = it.ownerId,
                games = selectFrom(GAME).where(TABLE.ID.eq(it.id)).fetchInto(Game::class.java)
            )
        }
    }

    override fun getTables(): List<Table> = withContext {
        selectFrom(TABLE).fetch().map {
            Table(
                id = it.id,
                name = it.name,
                ownerId = it.ownerId,
                games = selectFrom(GAME).where(TABLE.ID.eq(it.id)).fetchInto(Game::class.java)
            )
        }
    }

    override fun updateTable(updatedTable: Table): Unit = withContext {
        val tableRecord = selectFrom(TABLE).where(TABLE.ID.eq(updatedTable.id)).fetchOne()
        checkNotNull(tableRecord)

        tableRecord.name = tableRecord.name
        tableRecord.ownerId = tableRecord.ownerId
        tableRecord.store()
    }

    override fun saveTable(newTable: Table): Unit = withContext {
        val newRecord = newRecord(TABLE).apply {
            this.id = newTable.id
            this.name = newTable.name
            this.ownerId = newTable.ownerId
        }
        newRecord.store()
    }
}
