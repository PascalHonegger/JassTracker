package dev.honegger.repositories

import dev.honegger.domain.Game
import dev.honegger.domain.Table
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Disabled
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TableRepositoryImplTest {
    @Test
    @Disabled
    fun `getTable returns correct table after saveTable is called`() {
        val repository = TableRepositoryImpl()
        val newTable = Table(
            id = UUID.randomUUID(),
            name = "Some Name",
            ownerId = UUID.randomUUID(),
            games = listOf(Game(id = UUID.randomUUID(), startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)))
        )
        assertNull(repository.getTableOrNull(newTable.id))
        repository.saveTable(newTable)
        assertEquals(newTable, repository.getTableOrNull(newTable.id))
    }

    @Test
    @Disabled
    fun `getTables returns multiple tables after saveTable is called`() {
        val repository = TableRepositoryImpl()
        val table1 = Table(
            id = UUID.randomUUID(),
            name = "Foo",
            ownerId = UUID.randomUUID(),
            games = listOf(Game(id = UUID.randomUUID(), startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)))
        )
        val table2 = Table(
            id = UUID.randomUUID(),
            name = "Bar",
            ownerId = UUID.randomUUID(),
            games = listOf(Game(id = UUID.randomUUID(), startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)))

        )
        assertEquals(emptyList(), repository.getTables())
        repository.saveTable(table1)
        repository.saveTable(table2)
        assertEquals(listOf(table1, table2), repository.getTables())
    }
}
