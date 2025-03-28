package dev.honegger.jasstracker.bootstrap.plugins

import dev.honegger.jasstracker.bootstrap.utils.readConfiguration
import kotlin.test.*

class ConfigurationTest {
    @Test
    fun `should provide default configuration`() {
        assertContentEquals(listOf(
            "jasstracker.db.url" to "jdbc:postgresql://localhost:5432/jasstracker",
            "jasstracker.db.user" to "jasstracker",
            "jasstracker.db.password" to "password",
            "jwt.secret" to "z8TxaimeeD3R9EFBCBTLNi6LNlDOOiRuKjb5TYcUEcNNjYDzhbS5StLIB1wqvDPhNoXY66FUvIsQrOykDUbUQg==",
            "jwt.issuer" to "http://0.0.0.0:8080/",
            "jwt.audience" to "http://0.0.0.0:9090/",
            "jwt.realm" to "JassTracker",
            "jwt.expiryTime" to "4h",
            "hash.iterations" to "10",
            "hash.memory" to "65536",
            "hash.parallelization" to "1",
        ), readConfiguration { null })
    }

    @Test
    fun `should read environment variables`() {
        assertContentEquals(listOf(
            "jasstracker.db.url" to "DB_URL_VALUE",
            "jasstracker.db.user" to "DB_USER_VALUE",
            "jasstracker.db.password" to "DB_PASSWORD_VALUE",
            "jwt.secret" to "JWT_SECRET_VALUE",
            "jwt.issuer" to "JWT_ISSUER_VALUE",
            "jwt.audience" to "JWT_AUDIENCE_VALUE",
            "jwt.realm" to "JWT_REALM_VALUE",
            "jwt.expiryTime" to "JWT_EXPIRY_TIME_VALUE",
            "hash.iterations" to "HASH_ITERATIONS_VALUE",
            "hash.memory" to "HASH_MEMORY_VALUE",
            "hash.parallelization" to "HASH_PARALLELIZATION_VALUE",
        ), readConfiguration { "${it}_VALUE" })
    }
}