package dev.honegger

import dev.honegger.endpoints.configureSampleEndpoints
import io.ktor.server.application.*
import dev.honegger.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureHTTP()
    configureStaticRouting()
    configureSampleEndpoints()
}
