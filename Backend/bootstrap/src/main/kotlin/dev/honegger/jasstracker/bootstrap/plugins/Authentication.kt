package dev.honegger.jasstracker.bootstrap.plugins

import dev.honegger.jasstracker.security.JwtTokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication(jwtTokenService: JwtTokenService) {
    install(Authentication) {
        jwt {
            realm = jwtTokenService.realm
            verifier(jwtTokenService.tokenVerifier)
            validate { JWTPrincipal(it.payload) }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
