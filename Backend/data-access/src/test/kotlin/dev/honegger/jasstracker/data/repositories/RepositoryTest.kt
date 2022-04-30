package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.data.bootstrap
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class RepositoryTest {
    companion object {
        @Container
        @JvmStatic
        val postgresql = PostgreSQLContainer("postgres:14-alpine")

        @BeforeAll
        @JvmStatic
        fun setup() {
            bootstrap(postgresql.jdbcUrl, postgresql.username, postgresql.password, true)
        }
    }
}