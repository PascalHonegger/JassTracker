package dev.honegger.jasstracker.bootstrap.plugins

import dev.honegger.jasstracker.api.util.configureExceptionStatusCodes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.github.oshai.kotlinlogging.KotlinLogging

fun Application.configureHTTP() {
    val log = KotlinLogging.logger { }
    if (developmentMode) {
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

    install(StatusPages) {
        configureExceptionStatusCodes {_, cause ->
            log.error(cause) { null }
        }
    }
}
