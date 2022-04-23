package dev.honegger.jasstracker.bootstrap.plugins

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

            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Patch)
            allowMethod(HttpMethod.Delete)

            allowHost("localhost:9090")
            allowHost("0.0.0.0:9090")

            allowCredentials = true
            allowNonSimpleContentTypes = true
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
