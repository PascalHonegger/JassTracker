package dev.honegger.jasstracker.security

import de.mkammerer.argon2.Argon2Factory
import dev.honegger.jasstracker.domain.services.PasswordHashService

data class Argon2HashConfig(
    val iterations: Int,
    val memory: Int,
    val parallelization: Int,
)

class Argon2PasswordHashService(private val argon2HashConfig: Argon2HashConfig) : PasswordHashService {

    private val argon2 = Argon2Factory.create()

    override fun hashPassword(password: String): String =
        argon2.hash(
            argon2HashConfig.iterations,
            argon2HashConfig.memory,
            argon2HashConfig.parallelization,
            password.toCharArray()
        )

    override fun verifyPassword(hash: String, password: String): Boolean =
        argon2.verify(hash, password.toCharArray())
}
