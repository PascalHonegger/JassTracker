package dev.honegger.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.request.*

fun Application.configureHTTP() {
    if (environment.developmentMode) {
        install(CORS) {
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            allowCredentials = true
            allowHost("localhost:9090")
        }
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
