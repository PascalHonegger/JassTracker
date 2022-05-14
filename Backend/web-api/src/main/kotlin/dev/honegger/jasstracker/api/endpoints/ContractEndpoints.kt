package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.domain.services.ContractService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureContractEndpoints(contractService: ContractService) {
    route("/contracts") {
        get {
            val contracts = contractService.getContracts(call.playerSession())
            call.respond(HttpStatusCode.OK, contracts.map { it.toWebContract() })
        }
    }
}
