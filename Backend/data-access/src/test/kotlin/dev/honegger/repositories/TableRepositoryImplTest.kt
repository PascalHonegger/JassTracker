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
}
