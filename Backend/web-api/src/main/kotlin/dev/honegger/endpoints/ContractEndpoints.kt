package dev.honegger.endpoints

import dev.honegger.services.ContractService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureContractEndpoints(contractService: ContractService) {
    routing {
        route("/api/contracts") {
            get {
                val contracts = contractService.getContracts(dummySession)
                call.respond(HttpStatusCode.OK, contracts.map { it.toWebContract() })
            }
        }
    }
}
