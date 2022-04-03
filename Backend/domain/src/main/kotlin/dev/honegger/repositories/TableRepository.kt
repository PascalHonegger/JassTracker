package dev.honegger.repositories

import dev.honegger.domain.Table
import java.util.*

interface TableRepository {
    fun getTableOrNull(id: UUID): Table?
    fun getTables(): List<Table>
    fun updateTable(updatedTable: Table)
    fun saveTable(newTable: Table)
}
