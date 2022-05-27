package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.util.playerSession
import dev.honegger.jasstracker.api.util.respondNullable
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
            call.respond(tables.map { it.toWebTable() })
        }
        get("/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            val table = tableService.getTableOrNull(call.playerSession(), id.toUUID())
            call.respondNullable(table?.toWebTable())
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
            checkNotNull(id)
            val updatedTable = call.receive<WebTable>().toTable()
            tableService.updateTable(call.playerSession(), updatedTable)
            call.respond(HttpStatusCode.NoContent)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            checkNotNull(id)
            tableService.deleteTableById(call.playerSession(), id.toUUID())
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
