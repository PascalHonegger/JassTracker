package dev.honegger.jasstracker.api.endpoints

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import java.util.*
import kotlin.time.Duration.Companion.days

internal fun Application.installJson() {
    install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
        json()
    }
}

internal fun Application.installSecuredRoute(block: Route.() -> Unit) {
    install(Authentication) {
        jwt {
            realm = testRealm
            verifier(testTokenVerifier)
            validate { JWTPrincipal(it.payload) }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        authenticate {
            block()
        }
    }
}

internal fun HttpClientConfig<out HttpClientEngineConfig>.installJson() {
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
        json()
    }
}

internal fun HttpClientConfig<out HttpClientEngineConfig>.addJwtHeader(playerSession: PlayerSession = dummySession) {
    install(DefaultRequest) {
        bearerAuth(createToken(playerSession))
    }
}

// Test sessions
val dummySession = PlayerSession(
    playerId = "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
    isGuest = false,
    username = "dummy"
)

// Test config
private const val testRealm = "UnitTest"
private const val testIssuer = "UnitTest Server"
private const val testAudience = "UnitTest Client"
private const val testSecret =
    "z8TxaimeeD3R9EFBCBTLNi6LNlDOOiRuKjb5TYcUEcNNjYDzhbS5StLIB1wqvDPhNoXY66FUvIsQrOykDUbUQg=="

private fun createToken(playerSession: PlayerSession) = JWT.create()
    .withIssuer(testIssuer)
    .withAudience(testAudience)
    .withClaim(PlayerSession::playerId.name, playerSession.playerId.toString())
    .withClaim(PlayerSession::isGuest.name, playerSession.isGuest)
    .withClaim(PlayerSession::username.name, playerSession.username)
    .withExpiresAt(Date.from((Clock.System.now() + 1.days).toJavaInstant()))
    .sign(Algorithm.HMAC256(testSecret))

private val testTokenVerifier: JWTVerifier = JWT
    .require(Algorithm.HMAC256(testSecret))
    .withIssuer(testIssuer)
    .withAudience(testAudience)
    .build()
