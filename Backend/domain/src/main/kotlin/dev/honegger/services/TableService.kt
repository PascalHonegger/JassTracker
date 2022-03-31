package dev.honegger.services

import dev.honegger.domain.Table
import dev.honegger.domain.UserSession
import dev.honegger.repositories.TableRepository

import mu.KotlinLogging
import java.util.*

interface TableService {
    fun createTable(session: UserSession, name: String): Table
    fun getTableOrNull(session: UserSession, id: String): Table?
    fun getTablesOrNull(session: UserSession): List<Table>?
    fun updateTable(session: UserSession, updatedTable: Table)
}

private val log = KotlinLogging.logger { }

class TableServiceImpl(private val tableRepository: TableRepository) :
    TableService {
    override fun createTable(
        session: UserSession,
        name: String,
    ): Table {
        // Example of user input validation
        check(name.length in 2..15) { "Name must be between 2 and 15 characters" }
        val newTable = Table(
            id = UUID.randomUUID().toString(),
            name = name,
            ownerId = session.userId,
        )

        // Example of a log message
        log.info { "Saving new table $newTable" }
        tableRepository.saveTable(newTable)
        return newTable
    }

    override fun getTableOrNull(
        session: UserSession,
        id: String,
    ): Table? {
        // Users can load any table they know the ID of
        return tableRepository.getTableOrNull(id)
    }

    override fun getTablesOrNull(
        session: UserSession
    ): List<Table>? {
        // Users can load all tables (that belong to them / they are a part of)
        return tableRepository.getTablesOrNull()
    }

    override fun updateTable(
        session: UserSession,
        updatedTable: Table,
    ) {
        val existingTable =
            tableRepository.getTableOrNull(updatedTable.id)
        // User can only update a table which exists and is owned by himself
        checkNotNull(existingTable)
        check(updatedTable.ownerId == session.userId)
        // Only copy name as an example if only partial update is allowed
        tableRepository.saveTable(existingTable.copy(name = updatedTable.name))
    }
}
