package dev.honegger.jasstracker.data.converters

import kotlinx.datetime.LocalDateTime as KotlinLocalDateTime
import org.junit.jupiter.api.Test
import java.time.LocalDateTime as JavaLocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LocalDateTimeConverterTest {
    @Test
    fun `fromType is Java LocalDateTime`() {
        assertEquals(JavaLocalDateTime::class.java, LocalDateTimeConverter().fromType())
    }

    @Test
    fun `toType is Kotlin LocalDateTime`() {
        assertEquals(KotlinLocalDateTime::class.java, LocalDateTimeConverter().toType())
    }

    @Test
    fun `from creates Kotlin LocalDateTime with same value`() {
        val input = JavaLocalDateTime.of(2022, 4, 20, 13, 37)
        val expected = KotlinLocalDateTime(2022, 4, 20, 13, 37)
        assertEquals(expected, LocalDateTimeConverter().from(input))
    }

    @Test
    fun `to creates Java LocalDateTime with same value`() {
        val input = KotlinLocalDateTime(2022, 4, 20, 13, 37)
        val expected = JavaLocalDateTime.of(2022, 4, 20, 13, 37)
        assertEquals(expected, LocalDateTimeConverter().to(input))
    }

    @Test
    fun `from null is null`() {
        assertNull(LocalDateTimeConverter().from(null))
    }

    @Test
    fun `to null is null`() {
        assertNull(LocalDateTimeConverter().to(null))
    }
}
