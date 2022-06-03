package dev.honegger.jasstracker.api.util

import dev.honegger.jasstracker.domain.exceptions.NotFoundException
import dev.honegger.jasstracker.domain.exceptions.UnauthorizedException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.configureExceptionStatusCodes(block: (call: ApplicationCall, cause: Throwable) -> Unit = {_,_ -> }) {
    exception<Throwable> { call, cause ->
        block(call, cause)
        val status = when (cause) {
            is IllegalArgumentException -> HttpStatusCode.BadRequest
            is UnauthorizedException -> HttpStatusCode.Unauthorized
            is io.ktor.server.plugins.NotFoundException, is NotFoundException -> HttpStatusCode.NotFound
            else -> HttpStatusCode.InternalServerError
        }
        call.respondText(text = cause.message ?: "", status = status)
    }
}
