package dev.honegger.jasstracker.security

interface PasswordHashService {
    fun hashPassword(password: String)
    fun verifyPassword(hash: String, password: String)
}