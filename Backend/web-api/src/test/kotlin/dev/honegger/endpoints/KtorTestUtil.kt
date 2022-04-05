package dev.honegger.endpoints

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*

internal fun Application.installJson() {
    install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
        json()
    }
}

internal fun HttpClientConfig<out HttpClientEngineConfig>.installJson() {
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
        json()
    }
}
