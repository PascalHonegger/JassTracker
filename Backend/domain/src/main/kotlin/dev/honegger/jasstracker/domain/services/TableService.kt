package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Table
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.repositories.TableRepository

import mu.KotlinLogging
import java.util.*

interface TableService {
    fun createTable(session: PlayerSession, name: String): Table
    fun getTableOrNull(session: PlayerSession, id: UUID): Table?
    fun getTables(session: PlayerSession): List<Table>
    fun updateTable(session: PlayerSession, updatedTable: Table)
    fun deleteTableById(session: PlayerSession, id: UUID): Boolean
}

private val log = KotlinLogging.logger { }

class TableServiceImpl(private val tableRepository: TableRepository) :
    TableService {
    override fun createTable(
        session: PlayerSession,
        name: String,
    ): Table {
        check(name.length in 2..30) { "Name must be between 2 and 30 characters" }
        val newTable = Table(
            id = UUID.randomUUID(),
            name = name,
            ownerId = session.playerId,
            games = emptyList(),
        )

        log.info { "Saving new table $newTable" }
        tableRepository.saveTable(newTable)
        return newTable
    }

    override fun getTableOrNull(
        session: PlayerSession,
        id: UUID,
    ): Table? {
        // Users can load any table they know the ID of
        return tableRepository.getTableOrNull(id)
    }

    override fun getTables(
        session: PlayerSession
    ): List<Table> {
        // Users can load all tables (that belong to them / they are a part of)
        return tableRepository.getTables(session.playerId)
    }

    override fun updateTable(
        session: PlayerSession,
        updatedTable: Table,
    ) {
        val existingTable =
            tableRepository.getTableOrNull(updatedTable.id)
        // User can only update a table which exists and is owned by himself
        checkNotNull(existingTable)
        check(updatedTable.ownerId == session.playerId)
        tableRepository.saveTable(existingTable.copy(name = updatedTable.name))
    }

    override fun deleteTableById(session: PlayerSession, id: UUID): Boolean {
        val existingTable =
            tableRepository.getTableOrNull(id)
        checkNotNull(existingTable)
        check(existingTable.ownerId == session.playerId)
        return tableRepository.deleteTableById(id)
    }
}
