package dev.honegger.jasstracker.api.endpoints

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.honegger.jasstracker.domain.services.AuthenticationService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Route.configureAuthenticationEndpoints(
    authenticationService: AuthenticationService,
) {
    post {
        val token = JWT.create()
            .withAudience()
            .withClaim("username", "TODO")
            //.withExpiresAt()
            .sign(Algorithm.HMAC256("TODO"))
        call.respond(hashMapOf("token" to token))
    }

    authenticate("auth-jwt") {
        get("/overview") {
            val principal = call.principal<JWTPrincipal>()

        }
    }

}