package dev.honegger.jasstracker.bootstrap.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication () {
    environment.config.apply {
        install(Authentication) {
            jwt {
                realm = property("jwt.realm").getString()
                verifier(JWT
                    .require(Algorithm.HMAC256(property("jwt.secret").getString()))
                    .withAudience(property("jwt.audience").getString())
                    .withIssuer(property("jwt.issuer").getString())
                    .build())
                validate { credential ->
                    if (credential.payload.getClaim("username").asString() != "") {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
                challenge { defaultScheme, realm ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
        }
    }
}