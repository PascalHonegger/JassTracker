package dev.honegger.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*

fun Application.configureHTTP() {
    install(CORS) {
        header(HttpHeaders.ContentType)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        host("localhost")
    }

    install(AutoHeadResponse)

    install(ContentNegotiation) {
        json()
    }

    install(CallLogging) {
        filter { call ->
            call.request.path().startsWith("/api")
        }

        // Configure MDC to include cool info like sessionId
    }
}
