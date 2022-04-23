package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Table
import dev.honegger.jasstracker.domain.UserSession
import dev.honegger.jasstracker.domain.repositories.TableRepository
import io.mockk.*
import java.util.*
import kotlin.test.*

class TableServiceImplTest {
    private val repository = mockk<TableRepository>()
    private val service = TableServiceImpl(repository)
    private val dummySession = UserSession(UUID.randomUUID(), "dummy")
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
        assertEquals(dummySession.userId, created.ownerId)
        verify(exactly = 1) { repository.saveTable(any()) }
    }
}
