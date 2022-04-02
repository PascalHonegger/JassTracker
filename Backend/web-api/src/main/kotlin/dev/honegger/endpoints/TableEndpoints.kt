package dev.honegger.endpoints

import dev.honegger.services.TableService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureTableEndpoints(
    // Could be injected by a DI framework like Koin
    tableService: TableService,
) {
    routing {
        route("/api/tables") {
            get {
                val tables = tableService.getTables(dummySession)
                call.respond(HttpStatusCode.OK, tables.map { it.toWebTable() })
            }
            get("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val table =
                    tableService.getTableOrNull(dummySession, UUID.fromString(id))

                if (table == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(HttpStatusCode.OK, table.toWebTable())
            }
            put {
                val newTable = call.receive<WebCreateTable>()
                val createdTable = tableService.createTable(
                    dummySession,
                    newTable.name
                )
                call.respond(HttpStatusCode.Created, createdTable.id)
            }
            post("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val updatedTable = call.receive<WebTable>().toTable()
                tableService.updateTable(dummySession, updatedTable)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
