package dev.honegger

import dev.honegger.endpoints.configureGameEndpoints
import dev.honegger.plugins.configureHTTP
import dev.honegger.plugins.configureStaticRouting
import dev.honegger.plugins.initializeDatabase
import dev.honegger.repositories.GameRepositoryImpl
import dev.honegger.services.GameServiceImpl
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    initializeDatabase()
    configureHTTP()
    configureStaticRouting()

    // Could be provided by a DI framework like Koin
    val gameRepository = GameRepositoryImpl()
    val gameService = GameServiceImpl(gameRepository)
    configureGameEndpoints(gameService)
}
