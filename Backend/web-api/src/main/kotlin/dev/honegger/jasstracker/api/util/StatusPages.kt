package dev.honegger.jasstracker.api.util

import dev.honegger.jasstracker.domain.exceptions.NotFoundException
import dev.honegger.jasstracker.domain.exceptions.UnauthorizedException
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.configureExceptionStatusCodes() =
    exception<Throwable> { call, cause ->
        val status = when (cause) {
            is IllegalArgumentException -> HttpStatusCode.BadRequest
            is UnauthorizedException -> HttpStatusCode.Unauthorized
            is io.ktor.server.plugins.NotFoundException, is NotFoundException -> HttpStatusCode.NotFound
            else -> HttpStatusCode.InternalServerError
        }
        call.respondText(text = cause.message ?: "", status = status)
    }
