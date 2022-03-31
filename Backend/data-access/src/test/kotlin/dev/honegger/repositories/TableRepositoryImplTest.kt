package dev.honegger.repositories

import dev.honegger.domain.Table
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TableRepositoryImplTest {
    @Test
    fun `getTable returns correct table after saveTable is called`() {
        val repository = TableRepositoryImpl()
        val newTable = Table(
            id = "Some ID",
            name = "Some Name",
            ownerId = "Some owner",
        )
        assertNull(repository.getTableOrNull(newTable.id))
        repository.saveTable(newTable)
        assertEquals(newTable, repository.getTableOrNull(newTable.id))
    }

    @Test
    fun `getTables returns multiple tables after saveTable is called`() {
        val repository = TableRepositoryImpl()
        val table1 = Table(
            id = "1",
            name = "Foo",
            ownerId = "Some owner",
        )
        val table2 = Table(
            id = "2",
            name = "Bar",
            ownerId = "Some owner",
        )
        assertEquals(emptyList(), repository.getTablesOrEmpty())
        repository.saveTable(table1)
        repository.saveTable(table2)
        assertEquals(listOf(table1, table2), repository.getTablesOrEmpty())
    }
}
