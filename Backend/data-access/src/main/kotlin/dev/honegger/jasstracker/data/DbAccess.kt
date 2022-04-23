package dev.honegger.jasstracker.data

import org.jooq.DSLContext
import org.jooq.impl.DSL

private lateinit var storedUrl: String
private lateinit var storedUsername: String
private var storedPassword: String? = null

internal fun initializeDbConnection(
    url: String,
    user: String,
    password: String?,
) {
    storedUrl = url
    storedUsername = user
    storedPassword = password
}

internal fun <R> withContext(block: DSLContext.() -> R): R = DSL.using(storedUrl, storedUsername, storedPassword).use {
    it.block()
}
