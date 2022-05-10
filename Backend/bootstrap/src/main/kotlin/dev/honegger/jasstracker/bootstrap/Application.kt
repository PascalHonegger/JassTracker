package dev.honegger.jasstracker.bootstrap

import dev.honegger.jasstracker.bootstrap.plugins.configureHTTP
import dev.honegger.jasstracker.bootstrap.plugins.configureStaticRouting
import dev.honegger.jasstracker.bootstrap.plugins.initializeDatabase
import dev.honegger.jasstracker.data.repositories.*
import dev.honegger.jasstracker.domain.services.*
import dev.honegger.jasstracker.api.endpoints.*
import dev.honegger.jasstracker.bootstrap.plugins.configureAuthentication
import dev.honegger.jasstracker.security.Argon2HashConfig
import dev.honegger.jasstracker.security.Argon2PasswordHashService
import dev.honegger.jasstracker.security.JwtConfig
import dev.honegger.jasstracker.security.JwtTokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import kotlin.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    environment.config.apply {
        val jwtConfig = JwtConfig(
            secret = property("jwt.secret").getString(),
            issuer = property("jwt.issuer").getString().let {  },
            audience = property("jwt.audience").getString(),
            realm = property("jwt.realm").getString(),
            expiryTime = property("jwt.expiryTime").getString().let { Duration.parse(it) },
        )
        val argon2HashConfig =
            Argon2HashConfig(
                iterations = property("hash.iterations").getString().toInt(),
                memory = property("hash.memory").getString().toInt(),
                parallelization = property("hash.parallelization").getString().toInt(),
            )

        // Could be provided by a DI framework like Koin
        val gameRepository = GameRepositoryImpl()
        val tableRepository = TableRepositoryImpl(gameRepository)
        val playerRepository = PlayerRepositoryImpl()
        val contractRepository = ContractRepositoryImpl()

        val authTokenService = JwtTokenService(jwtConfig)
        val passwordHashService = Argon2PasswordHashService(argon2HashConfig)
        val gameService = GameServiceImpl(gameRepository, playerRepository)
        val tableService = TableServiceImpl(tableRepository)
        val roundRepository = RoundRepositoryImpl()
        val roundService = RoundServiceImpl(roundRepository, tableRepository)
        val contractService = ContractServiceImpl(contractRepository)
        val playerService = PlayerServiceImpl(playerRepository, passwordHashService)

        initializeDatabase()
        configureHTTP()
        configureStaticRouting()
        configureAuthentication(authTokenService)

        routing {
            configureAuthenticationEndpoints(playerService, authTokenService)
            authenticate {
                configureGameEndpoints(gameService)
                configureTableEndpoints(tableService)
                configureContractEndpoints(contractService)
                configurePlayerEndpoints(playerService)
                configureRoundEndpoints(roundService)
            }
        }
    }
}
