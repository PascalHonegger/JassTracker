package dev.honegger.repositories

import dev.honegger.domain.Table

interface TableRepository {
    fun getTableOrNull(id: String): Table?
    fun getTablesOrNull(): List<Table>?
    fun saveTable(updatedTable: Table)
}
