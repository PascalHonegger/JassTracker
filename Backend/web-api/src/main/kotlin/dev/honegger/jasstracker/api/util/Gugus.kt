package dev.honegger.jasstracker.api.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend inline fun <reified T> ApplicationCall.respondNullable(
    response: T?,
    notNullStatusCode: HttpStatusCode = HttpStatusCode.OK,
    nullStatusCode: HttpStatusCode = HttpStatusCode.NotFound,
) {
    if (response == null)
        respond(nullStatusCode)
    else
        respond(notNullStatusCode, response)
}
