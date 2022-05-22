package dev.honegger.jasstracker.domain.util

import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UUIDTest {
    @Test
    fun `toUUID converts to UUID correctly`() {
        val str = "28d6dc01-2d19-41d7-a6c1-a2af7c7838c7"
        val uuid = str.toUUID()
        assertEquals(UUID.fromString(str), uuid)
        assertEquals(str, uuid.toString())
    }

    @Test
    fun `invalid UUID causes exception`() {
        val str = "not a UUID"
        assertThrows<IllegalArgumentException> { str.toUUID() }
    }
}
