package dev.honegger.jasstracker.security

import com.auth0.jwt.JWT

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWTVerifier
import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.services.AuthToken
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

private const val guestUsername = "Gast"

class JwtTokenService(private val jwtConfig: JwtConfig, private val clock: Clock = Clock.System) : AuthTokenService {
    override fun createToken(player: Player): AuthToken {
        return JWT.create()
            .withIssuedAt(clock.now().toJavaInstant().let { Date.from(it) })
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim(PlayerSession::playerId.name, player.id.toString())
            .withClaim(PlayerSession::isGuest.name, player is GuestPlayer)
            .withClaim(PlayerSession::username.name, if (player is RegisteredPlayer) player.username else guestUsername)
            .withClaim("displayName", if (player is RegisteredPlayer) player.displayName else guestUsername)
            .withExpiresAt((clock.now() + jwtConfig.expiryTime).toJavaInstant().let { Date.from(it) })
            .sign(Algorithm.HMAC256(jwtConfig.secret))
            .let { AuthToken(it) }
    }

    val tokenVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.secret))
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()

    val realm: String = jwtConfig.realm
}
