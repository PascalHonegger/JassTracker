package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Table
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.repositories.TableRepository
import dev.honegger.jasstracker.domain.util.validateCurrentPlayer
import dev.honegger.jasstracker.domain.util.validateExists

import mu.KotlinLogging
import java.util.*
import kotlin.collections.HashMap

interface TableService {
    fun createTable(session: PlayerSession, name: String): Table
    fun getTableOrNull(session: PlayerSession, id: UUID): Table?
    fun getTables(session: PlayerSession): List<Table>
    fun updateTable(session: PlayerSession, updatedTable: Table)
    fun deleteTableById(session: PlayerSession, id: UUID)
}

private val log = KotlinLogging.logger { }

class TableServiceImpl(private val tableRepository: TableRepository) : TableService {
    private fun validateTableName(name: String) = require(name.length in 2..30) { "Name must be between 2 and 30 characters" }

    override fun createTable(
        session: PlayerSession,
        name: String,
    ): Table {
        validateTableName(name)
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
        val existingTable = tableRepository.getTableOrNull(updatedTable.id)
        validateExists(existingTable) { "Player can only update a table which exists and is owned by himself" }
        validateCurrentPlayer(updatedTable.ownerId, session) { "Player can only update hiw own table" }
        validateTableName(updatedTable.name)
        tableRepository.updateTable(existingTable.copy(name = updatedTable.name))
    }

    override fun deleteTableById(session: PlayerSession, id: UUID) {
        val existingTable = tableRepository.getTableOrNull(id)
        validateExists(existingTable) { "Table $id does not exist" }
        validateCurrentPlayer(existingTable.ownerId, session) { "Only table owner can delete table" }
        val wasDeleted = tableRepository.deleteTableById(id)
        if (!wasDeleted) {
            log.warn { "Delete table $id did not result in DB update" }
        }
    }
}
