package dev.honegger.jasstracker.domain.repositories

import dev.honegger.jasstracker.domain.Table
import java.util.*

interface TableRepository {
    fun getTableOrNull(id: UUID): Table?
    fun getTableByGameIdOrNull(id: UUID): Table?
    fun getTables(ownerId: UUID): List<Table>
    fun updateTable(updatedTable: Table)
    fun saveTable(newTable: Table)
    fun deleteTableById(id: UUID): Boolean
}
