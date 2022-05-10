package dev.honegger.jasstracker.security

import com.auth0.jwt.JWT

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWTVerifier
import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.services.AuthTokenService
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import java.util.*
import kotlin.time.Duration

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val expiryTime: Duration,
)

class JwtTokenService(private val jwtConfig: JwtConfig, private val clock: Clock = Clock.System) : AuthTokenService {
    override fun createToken(player: Player): String {
        return JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("PlayerId", player.id.toString())
            .withClaim("IsGuest", player is GuestPlayer)
            .withClaim("Username", if (player is RegisteredPlayer) player.username else "Gast")
            .withExpiresAt(Date.from((clock.now() + jwtConfig.expiryTime).toJavaInstant()))
            .sign(Algorithm.HMAC256(jwtConfig.secret))
    }

    val tokenVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.secret))
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()

    val realm: String = jwtConfig.realm
}
