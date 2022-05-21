package dev.honegger.jasstracker.security

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.RegisteredPlayer
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class JwtTokenServiceTest {
    private val config = JwtConfig(
        secret = "z8TxaimeeD3R9EFBCBTLNi6LNlDOOiRuKjb5TYcUEcNNjYDzhbS5StLIB1wqvDPhNoXY66FUvIsQrOykDUbUQg==",
        issuer = "Test Issuer",
        audience = "Test Audience",
        realm = "JwtTokenServiceTest",
        expiryTime = 2.hours,
    )
    private val registeredPlayer = RegisteredPlayer(
        id = UUID.randomUUID(),
        username = "robert",
        displayName = "Robert",
        password = "RobertsPassword_hash"
    )
    private val guestPlayer = GuestPlayer(id = UUID.randomUUID())
    private val service = JwtTokenService(config)

    @Test
    fun `test createToken for RegisteredPlayer`() {
        val playerToken = service.createToken(registeredPlayer).token
        val jwt = service.tokenVerifier.verify(playerToken)
        assertEquals(registeredPlayer.id.toString(), jwt.getClaim(PlayerSession::playerId.name).asString())
        assertEquals(registeredPlayer.username, jwt.getClaim(PlayerSession::username.name).asString())
        assertEquals(false, jwt.getClaim(PlayerSession::isGuest.name).asBoolean())
    }

    @Test
    fun `test createToken for GuestPlayer`() {
        val playerToken = service.createToken(guestPlayer).token
        val jwt = service.tokenVerifier.verify(playerToken)
        assertEquals(guestPlayer.id.toString(), jwt.getClaim(PlayerSession::playerId.name).asString())
        assertEquals("Gast", jwt.getClaim(PlayerSession::username.name).asString())
        assertEquals(true, jwt.getClaim(PlayerSession::isGuest.name).asBoolean())
    }

}
