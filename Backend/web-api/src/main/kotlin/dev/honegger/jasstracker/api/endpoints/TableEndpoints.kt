package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.domain.services.TableService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureTableEndpoints(
    tableService: TableService,
) {
    route("/tables") {
        get {
            val tables = tableService.getTables(call.playerSession())
            call.respond(HttpStatusCode.OK, tables.map { it.toWebTable() })
        }
        get("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val table =
                tableService.getTableOrNull(call.playerSession(), id.toUUID())

            if (table == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(HttpStatusCode.OK, table.toWebTable())
        }
        post {
            val newTable = call.receive<WebCreateTable>()
            val createdTable = tableService.createTable(
                call.playerSession(),
                newTable.name
            )
            call.respond(HttpStatusCode.Created, createdTable.toWebTable())
        }
        put("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val updatedTable = call.receive<WebTable>().toTable()
            tableService.updateTable(call.playerSession(), updatedTable)
            call.respond(HttpStatusCode.OK)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val success = tableService.deleteTableById(call.playerSession(), id.toUUID())
            if (!success) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
