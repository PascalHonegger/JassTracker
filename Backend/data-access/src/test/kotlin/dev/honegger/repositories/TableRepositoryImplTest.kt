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
            games = listOf(
                Game(
                    id = UUID.randomUUID(),
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    rounds = emptyList()
                )
            )
        )
        assertNull(repository.getTableOrNull(newTable.id))
        repository.saveTable(newTable)
        assertEquals(newTable, repository.getTableOrNull(newTable.id))
    }

    @Test
    @Disabled
    fun `getTables returns multiple tables after saveTable is called`() {
        val repository = TableRepositoryImpl()
        val owner1 = UUID.randomUUID()
        val owner2 = UUID.randomUUID()
        val table1 = Table(
            id = UUID.randomUUID(),
            name = "Foo",
            ownerId = owner1,
            games = listOf(
                Game(
                    id = UUID.randomUUID(),
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    rounds = emptyList()
                )
            )
        )
        val table2 = Table(
            id = UUID.randomUUID(),
            name = "Bar",
            ownerId = owner1,
            games = listOf(
                Game(
                    id = UUID.randomUUID(),
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    rounds = emptyList()
                )
            )
        )
        val table3 = Table(
            id = UUID.randomUUID(),
            name = "Other owner Table",
            ownerId = owner2,
            games = emptyList()
        )
        assertEquals(emptyList(), repository.getTables(owner1))
        repository.saveTable(table1)
        repository.saveTable(table2)
        assertEquals(listOf(table1, table2), repository.getTables(owner1))
        assertEquals(listOf(table3), repository.getTables(owner2))
    }
}
