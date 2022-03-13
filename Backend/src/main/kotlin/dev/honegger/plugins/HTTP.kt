package dev.honegger.plugins

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    install(CORS) {
        header(HttpHeaders.ContentType)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        host("localhost")
    }

    install(AutoHeadResponse)
}
