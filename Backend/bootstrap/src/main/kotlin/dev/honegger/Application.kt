package dev.honegger

import dev.honegger.endpoints.configureScoreboardEndpoints
import dev.honegger.plugins.configureHTTP
import dev.honegger.plugins.configureStaticRouting
import dev.honegger.plugins.runDbMigrations
import dev.honegger.repositories.ScoreboardRepositoryImpl
import dev.honegger.services.ScoreboardServiceImpl
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    runDbMigrations()
    configureHTTP()
    configureStaticRouting()

    // Could be provided by a DI framework like Koin
    val scoreboardRepository = ScoreboardRepositoryImpl()
    val scoreboardService = ScoreboardServiceImpl(scoreboardRepository)
    configureScoreboardEndpoints(scoreboardService)
}
