package dev.honegger.jasstracker.domain.services

interface PasswordHashService {
    fun hashPassword(password: String)
    fun verifyPassword(hash: String, password: String): Boolean
}