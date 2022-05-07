package dev.honegger.jasstracker.security

import de.mkammerer.argon2.Argon2Factory

data class Argon2HashConfig(val iterations: String, val memory: String, val parallelization: String)

class Argon2PasswordHashService(private val argon2HashConfig: Argon2HashConfig) : PasswordHashService {

    private val argon2 = Argon2Factory.create()

    override fun hashPassword(password: String) {
        argon2.hash(argon2HashConfig.iterations.toInt(),
            argon2HashConfig.memory.toInt(),
            argon2HashConfig.parallelization.toInt(),
            password.toCharArray())
    }

    override fun verifyPassword(hash: String, password: String) {
        argon2.verify(hash, password.toCharArray())
    }

}