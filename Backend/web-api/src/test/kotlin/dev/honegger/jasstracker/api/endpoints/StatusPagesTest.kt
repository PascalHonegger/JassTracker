package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.exceptions.NotFoundException
import dev.honegger.jasstracker.domain.exceptions.UnauthorizedException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class StatusPagesTest {
    @Test
    fun `UnauthorizedException returns 403`() = test(UnauthorizedException("wrong game owner"), HttpStatusCode.Unauthorized)

    @Test
    fun `NotFoundException returns 404`() = test(NotFoundException("game not found"), HttpStatusCode.NotFound)

    @Test
    fun `Ktor NotFoundException returns 404`() = test(io.ktor.server.plugins.NotFoundException(), HttpStatusCode.NotFound)

    @Test
    fun `IllegalArgumentException returns 400`() = test(IllegalArgumentException(), HttpStatusCode.BadRequest)

    @Test
    fun `IllegalStateException returns 500`() = test(IllegalStateException(), HttpStatusCode.InternalServerError)

    @Test
    fun `Other Throwable returns 500`() = test(object : Throwable() {}, HttpStatusCode.InternalServerError)

    private fun test(throwable: Throwable, statusCode: HttpStatusCode) = testApplication {
        application {
            installStatusPages()
        }
        routing {
            get("test") {
                throw throwable
            }
        }
        client.get("test").apply {
            assertEquals(statusCode, status)
            assertEquals(throwable.message ?: "", bodyAsText())
        }
    }
}
