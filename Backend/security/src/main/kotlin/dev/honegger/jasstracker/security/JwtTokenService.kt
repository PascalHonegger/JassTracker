package dev.honegger.jasstracker.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val expiryTime: String,
);

class JwtTokenService(private val jwtConfig: JwtConfig) : AuthTokenService {
    override fun createToken(username: String): String {
        val token = JWT.create()
            .withAudience(jwtConfig.audience)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.expiryTime.toInt()))
            .sign(Algorithm.HMAC256(jwtConfig.secret))
        return token
    }

    override fun validateToken() {
        TODO("Not yet implemented")
    }
}
