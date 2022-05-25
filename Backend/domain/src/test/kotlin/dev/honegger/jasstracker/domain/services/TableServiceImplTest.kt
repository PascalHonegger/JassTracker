package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Table
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.repositories.TableRepository
import io.mockk.*
import org.junit.jupiter.api.Disabled
import java.util.*
import kotlin.test.*

class TableServiceImplTest {
    private val repository = mockk<TableRepository>()
    private val service = TableServiceImpl(repository)
    private val dummySession = PlayerSession(UUID.randomUUID(), false, "dummy", "Dummy")
    private val passedTable = slot<Table>()

    @BeforeTest
    fun setup() {
        clearMocks(repository)
        passedTable.clear()
        every { repository.saveTable(capture(passedTable)) } just Runs
    }

    @AfterTest
    fun teardown() {
        confirmVerified(repository)
    }

    @Test
    fun `createTable creates table in repository`() {
        val dummyName = "Some Table"
        val created = service.createTable(dummySession, dummyName)

        assertTrue { passedTable.isCaptured }
        assertEquals(created, passedTable.captured)
        assertEquals(dummyName, created.name)
        assertEquals(dummySession.playerId, created.ownerId)
        verify(exactly = 1) { repository.saveTable(any()) }
    }

    @Test
    fun `deleteTableById removes a created table`() {
        val dummyName = "Some Table"
        val created = service.createTable(dummySession, dummyName)

        assertTrue { passedTable.isCaptured }
        assertEquals(created, passedTable.captured)
        assertEquals(dummyName, created.name)
        assertEquals(dummySession.playerId, created.ownerId)
        verify(exactly = 1) { repository.saveTable(created) }
        every {
            repository.getTableOrNull(created.id)
        } returns created

        every {
            repository.deleteTableById(created.id)
        } returns true

        val deleted = service.deleteTableById(dummySession, created.id)
        assertTrue { deleted }
        verify(exactly = 1) {
            repository.deleteTableById(created.id)
            repository.getTableOrNull(created.id)
        }
    }

    @Test
    @Disabled
    fun `deleteTableById removes a table, table can't be found afterwards anymore`() {
        val dummyName = "Some Table"
        val created = service.createTable(dummySession, dummyName)

        every {
            repository.getTableOrNull(created.id)
        } returns created

        every {
            repository.deleteTableById(created.id)
        } returns true

        val deleted = service.deleteTableById(dummySession, created.id)
        val tableShouldBeNull = service.getTableOrNull(dummySession, created.id)

        assertTrue { deleted }
        assertNull(tableShouldBeNull)

        verify(exactly = 1) {
            repository.deleteTableById(created.id)
            repository.getTableOrNull(created.id)
        }
    }
}
