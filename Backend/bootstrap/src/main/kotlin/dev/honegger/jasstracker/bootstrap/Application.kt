package dev.honegger.jasstracker.bootstrap

import dev.honegger.jasstracker.bootstrap.plugins.configureHTTP
import dev.honegger.jasstracker.bootstrap.plugins.configureStaticRouting
import dev.honegger.jasstracker.bootstrap.plugins.initializeDatabase
import dev.honegger.jasstracker.data.repositories.*
import dev.honegger.jasstracker.domain.services.*
import dev.honegger.jasstracker.api.endpoints.*
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
    val tableRepository = TableRepositoryImpl(gameRepository)
    val playerRepository = PlayerRepositoryImpl()
    val contractRepository = ContractRepositoryImpl()

    val gameService = GameServiceImpl(gameRepository, playerRepository)
    val tableService = TableServiceImpl(tableRepository)
    val roundRepository = RoundRepositoryImpl()
    val roundService = RoundServiceImpl(roundRepository, tableRepository)
    val contractService = ContractServiceImpl(contractRepository)
    val playerService = PlayerServiceImpl(playerRepository)

    configureGameEndpoints(gameService)
    configureTableEndpoints(tableService)
    configureContractEndpoints(contractService)
    configurePlayerEndpoints(playerService)
    configureRoundEndpoints(roundService)
}
