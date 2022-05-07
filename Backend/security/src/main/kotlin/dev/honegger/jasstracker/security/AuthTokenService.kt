package dev.honegger.jasstracker.security

interface AuthTokenService {
    fun createToken(username: String): String
    fun validateToken()
}