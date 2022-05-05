package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.services.ContractService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureContractEndpoints(contractService: ContractService) {
    route("/api/contracts") {
        get {
            val contracts = contractService.getContracts(dummySession)
            call.respond(HttpStatusCode.OK, contracts.map { it.toWebContract() })
        }
    }
}
