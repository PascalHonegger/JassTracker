package dev.honegger.jasstracker.api.serializer

import dev.honegger.jasstracker.domain.util.toUUID
import io.mockk.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.test.Test
import kotlin.test.assertEquals

class UUIDSerializerTest {
    @Test
    fun `serialize represents UUID as string`() {
        val uuid = "20c3f6d1-653f-4784-96c5-1c362f6dd243".toUUID()
        val encoder = mockk<Encoder>()
        every { encoder.encodeString(any()) } just Runs
        UUIDSerializer.serialize(encoder, uuid)
        verify(exactly = 1) { encoder.encodeString("20c3f6d1-653f-4784-96c5-1c362f6dd243") }
    }
    @Test
    fun `deserialize gets UUID from string`() {
        val uuid = "20c3f6d1-653f-4784-96c5-1c362f6dd243".toUUID()
        val decoder = mockk<Decoder>()
        every { decoder.decodeString() } returns "20c3f6d1-653f-4784-96c5-1c362f6dd243"
        val deserialized = UUIDSerializer.deserialize(decoder)
        assertEquals(uuid, deserialized)
        verify(exactly = 1) { decoder.decodeString() }
    }
}
