package dev.honegger.jasstracker.domain.util

import java.util.*

fun String.toUUID(): UUID = UUID.fromString(this)
