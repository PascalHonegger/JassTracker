package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.UserSession
import mu.KotlinLogging
import java.util.*

interface AuthenticationService {
    fun createUserSession(userId: UUID, username: String, token: String): UserSession
    fun authenticate(): Boolean
}

private val log = KotlinLogging.logger { }










