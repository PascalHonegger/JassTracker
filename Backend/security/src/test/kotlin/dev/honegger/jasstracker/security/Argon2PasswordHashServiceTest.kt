package dev.honegger.jasstracker.security

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Argon2PasswordHashServiceTest {
    private val dummyPassword = "This is a very s3curé p@ssw0rd"

    private val service = Argon2PasswordHashService(
        argon2HashConfig = Argon2HashConfig(
            iterations = 10,
            memory = 65536,
            parallelization = 1
        )
    )

    @Test
    fun `hashPassword returns a different hash for every call`() {
        val firstHash = service.hashPassword(dummyPassword)
        val secondHash = service.hashPassword(dummyPassword)
        assertNotEquals(firstHash, secondHash)
    }

    @Test
    fun `verifyPassword returns true for correct password`() {
        val hash = service.hashPassword(dummyPassword)
        assertTrue { service.verifyPassword(hash, dummyPassword) }
    }

    @Test
    fun `verifyPassword returns false for wrong password`() {
        val hash = service.hashPassword(dummyPassword)
        assertFalse { service.verifyPassword(hash, "Some other password") }
    }
}
