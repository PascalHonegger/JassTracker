package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipation
import dev.honegger.jasstracker.domain.Table
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.domain.util.toUUID
import kotlinx.datetime.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TableRepositoryImplTest : RepositoryTest() {
    private val repo = TableRepositoryImpl(GameRepositoryImpl())

    @Test
    fun `getTable returns correct table after saveTable is called`() {
        val newTable = Table(
            id = UUID.randomUUID(),
            name = "Some Name",
            ownerId = "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
            games = emptyList()
        )
        assertNull(repo.getTableOrNull(newTable.id))
        repo.saveTable(newTable)
        assertEquals(newTable, repo.getTableOrNull(newTable.id))
    }

    @Test
    fun `getTables returns multiple tables after saveTable is called`() {
        val owner1 = "283c0a20-b293-40e7-8858-da098a53b756".toUUID()
        val owner2 = "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID()
        val table1 = Table(
            id = UUID.randomUUID(),
            name = "Foo",
            ownerId = owner1,
            games = emptyList()
        )
        val table2 = Table(
            id = UUID.randomUUID(),
            name = "Bar",
            ownerId = owner1,
            games = emptyList()
        )
        val table3 = Table(
            id = UUID.randomUUID(),
            name = "Other owner Table",
            ownerId = owner2,
            games = emptyList()
        )
        assertEquals(emptyList(), repo.getTables(owner1))
        repo.saveTable(table1)
        repo.saveTable(table2)
        assertEquals(setOf(table1, table2), repo.getTables(owner1).toSet())
        repo.saveTable(table3)
        assertEquals(listOf(table3), repo.getTables(owner2))
    }

    @Test
    fun `getTableByGameIdOrNull returns table`() {
        val table = createTable()
        val gameId = UUID.randomUUID()
        val game = Game(
            gameId,
            LocalDateTime(2022, 4, 20, 13, 37),
            null,
            emptyList(),
            Team(GameParticipation("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(), "p1"),
                GameParticipation("3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(), "p2")),
            Team(GameParticipation("283c0a20-b293-40e7-8858-da098a53b756".toUUID(), "p3"),
                GameParticipation("cdfa5ae5-d182-4e11-b7f1-a173b2b4b797".toUUID(), "p4"))
        )
        GameRepositoryImpl().saveGame(game, table.id)
        val tableWithGame = table.copy(games = listOf(game))
        assertEquals(tableWithGame, repo.getTableByGameIdOrNull(gameId))
    }

    @Test
    fun `updateTable updates table`() {
        val table = createTable()
        val updatedTable = table.copy(name = "Nicer table")
        repo.updateTable(updatedTable)
        assertEquals(updatedTable, repo.getTableOrNull(table.id))
    }

    @Test
    fun `deleteTable deletes table`() {
        val id = createTable().id
        repo.deleteTableById(id)
        assertNull(repo.getTableOrNull(id))
    }

    private fun createTable(): Table {
        val table = Table(
            UUID.randomUUID(),
            "Nice Table",
            "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
            emptyList()
        )
        repo.saveTable(table)
        return table
    }
}
