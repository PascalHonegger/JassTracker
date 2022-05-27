package dev.honegger.jasstracker.domain.util

import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.exceptions.NotFoundException
import dev.honegger.jasstracker.domain.exceptions.UnauthorizedException
import java.util.UUID
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

inline fun validateCurrentPlayer(id: UUID, session: PlayerSession, lazyMessage: () -> String) {
    if (id != session.playerId) {
        throw UnauthorizedException(lazyMessage())
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T> validateExists(value: T?, lazyMessage: () -> String) {
    contract {
        returns() implies (value != null)
    }
    if (value == null) {
        throw NotFoundException(lazyMessage())
    }
}
