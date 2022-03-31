package dev.honegger.endpoints

import dev.honegger.domain.Table
import dev.honegger.domain.UserSession
import dev.honegger.services.TableService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

// These objects could be generated using something like OpenAPI
@Serializable
data class WebTable(val name: String, val ownerId: String)

@Serializable
data class WebCreateTable(val name: String)

// Would be loaded once authentication is implemented
private val dummySession = UserSession(userId = "42", "dummy")

// Map from WebTable to domain Table
private fun WebTable.toTable(id: String) = Table(
    id = id,
    name = name,
    ownerId = ownerId,
)

// Map from domain Table to WebTable
private fun Table.toWebTable() = WebTable(
    name = name,
    ownerId = ownerId,
)

fun Application.configureTableEndpoints(
    // Could be injected by a DI framework like Koin
    tableService: TableService,
) {
    routing {
        route("/api/tables") {
            get("/{id}") {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val table =
                    tableService.getTableOrNull(dummySession, id)

                if (table == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(HttpStatusCode.OK, table.toWebTable())
            }
            get("/") {
                val tables =
                    tableService.getTablesOrEmpty(dummySession)

                if (tables.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                val webTables = mutableListOf<WebTable>()
                tables.forEach {
                    webTables += it.toWebTable()
                }
                call.respond(HttpStatusCode.OK, webTables)
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
                val updatedTable = call.receive<WebTable>().toTable(id)
                tableService.updateTable(dummySession, updatedTable)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
