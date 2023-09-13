rootProject.name = "JassTracker"
include(":Backend:bootstrap")
include(":Backend:data-access")
include(":Backend:domain")
include(":Backend:web-api")
include(":Backend:security")

dependencyResolutionManagement {
    versionCatalogs {
        val kotlin = "1.9.10"
        val mockk = "1.13.4"
        val ktor = "2.2.3"
        val logback = "1.4.5"
        val shadow = "7.1.2"
        val kotlinxDatetime = "0.4.0"
        val kotlinLogging = "3.0.3"
        val flyway = "9.15.0"
        val postgresql = "42.5.4"
        val jooq = "3.16.5"
        val jooqPlugin = "8.1"
        val kover = "0.7.3"
        val testcontainers = "1.19.0"
        val javaJwt = "3.19.3"
        val argon2 = "2.11"
        val versions = "0.48.0"

        create("libs") {
            library("ktor-server-core", "io.ktor", "ktor-server-core-jvm").version(ktor)
            library("ktor-server-host-common", "io.ktor", "ktor-server-host-common-jvm").version(ktor)
            library("ktor-server-auto-head-response", "io.ktor", "ktor-server-auto-head-response-jvm").version(ktor)
            library("ktor-server-call-logging", "io.ktor", "ktor-server-call-logging").version(ktor)
            library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation-jvm").version(ktor)
            library("ktor-server-cors", "io.ktor", "ktor-server-cors-jvm").version(ktor)
            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json-jvm").version(ktor)
            library("ktor-server-netty", "io.ktor", "ktor-server-netty-jvm").version(ktor)
            library("ktor-client-content-negotiation", "io.ktor", "ktor-client-content-negotiation").version(ktor)
            library("ktor-server-auth", "io.ktor", "ktor-server-auth").version(ktor)
            library("ktor-server-auth-jwt", "io.ktor", "ktor-server-auth-jwt").version(ktor)
            library("ktor-server-status-pages", "io.ktor", "ktor-server-status-pages").version(ktor)
            library("kotlinx-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime-jvm").version(kotlinxDatetime)
            library("logback", "ch.qos.logback", "logback-classic").version(logback)
            library("kotlin-logging", "io.github.microutils", "kotlin-logging-jvm").version(kotlinLogging)
            library("flyway", "org.flywaydb", "flyway-core").version(flyway)
            library("postgresql", "org.postgresql", "postgresql").version(postgresql)
            library("jooq", "org.jooq", "jooq").version(jooq)
            library("java-jwt", "com.auth0", "java-jwt").version(javaJwt)
            library("argon2-jvm", "de.mkammerer", "argon2-jvm").version(argon2)
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").version(kotlin)

            version("jooq", jooq)

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version(kotlin)
            plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization").version(kotlin)
            plugin("shadow", "com.github.johnrengelman.shadow").version(shadow)
            plugin("jooq", "nu.studer.jooq").version(jooqPlugin)
            plugin("kover", "org.jetbrains.kotlinx.kover").version(kover)
            plugin("versions", "com.github.ben-manes.versions").version(versions)
        }
        create("testLibs") {
            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test-junit5").version(kotlin)
            library("ktor-server-tests", "io.ktor", "ktor-server-tests-jvm").version(ktor)
            library("mockk", "io.mockk", "mockk").version(mockk)
            library("testcontainers", "org.testcontainers", "testcontainers").version(testcontainers)
            library("testcontainers-junit", "org.testcontainers", "junit-jupiter").version(testcontainers)
            library("testcontainers-postgresql", "org.testcontainers", "postgresql").version(testcontainers)
        }
    }
}
