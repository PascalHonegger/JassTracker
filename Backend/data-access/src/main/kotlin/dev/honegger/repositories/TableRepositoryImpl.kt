package dev.honegger.repositories

import dev.honegger.domain.Table

class TableRepositoryImpl : TableRepository {
    private val tables = mutableMapOf<String, Table>()

    override fun getTableOrNull(id: String): Table? {
        // Placeholder code for DB access
        // This code would map the generated jOOQ-Entities to the domain object
        return tables[id]
    }

    override fun getTablesOrEmpty(): List<Table> {
        // Placeholder code for DB access
        // This code would map the generated jOOQ-Entities to the domain object
        return tables.values.toList()
    }

    override fun saveTable(updatedTable: Table) {
        // Placeholder code for DB access
        // This code would map the provided domain object to the generated jOOQ-Entities
        tables[updatedTable.id] = updatedTable
    }
}
