rootProject.name = "JassTracker"
include("Backend:bootstrap")
include("Backend:data-access")
include("Backend:domain")
include("Backend:web-api")

dependencyResolutionManagement {
    versionCatalogs {
        val kotlin = "1.6.20"
        val mockk = "1.12.3"
        val ktor = "2.0.0-beta-1"
        val logback = "1.2.11"
        val shadow = "7.1.2"
        val kotlinxDatetime = "0.3.2"
        val kotlinLogging = "2.1.21"
        val flyway = "8.5.5"
        val postgresql = "42.3.3"
        val jooq = "3.16.5"
        val jooqPlugin = "7.1.1"

        create("libs") {
            library("ktor-server-core", "io.ktor", "ktor-server-core-jvm").version(ktor)
            library("ktor-server-host-common", "io.ktor", "ktor-server-host-common-jvm").version(ktor)
            library("ktor-server-auto-head-response", "io.ktor", "ktor-server-auto-head-response-jvm").version(ktor)
            library("ktor-server-call-logging", "io.ktor", "ktor-server-call-logging").version(ktor)
            library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation-jvm").version(ktor)
            library("ktor-server-cors", "io.ktor", "ktor-server-cors-jvm").version(ktor)
            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json-jvm").version(ktor)
            library("ktor-server-netty", "io.ktor", "ktor-server-netty-jvm").version(ktor)
            library("kotlinx-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime-jvm").version(kotlinxDatetime)
            library("logback", "ch.qos.logback", "logback-classic").version(logback)
            library("kotlin-logging", "io.github.microutils", "kotlin-logging-jvm").version(kotlinLogging)
            library("flyway", "org.flywaydb", "flyway-core").version(flyway)
            library("postgresql", "org.postgresql", "postgresql").version(postgresql)
            library("jooq", "org.jooq", "jooq").version(jooq)

            version("jooq", jooq)

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version(kotlin)
            plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization").version(kotlin)
            plugin("shadow", "com.github.johnrengelman.shadow").version(shadow)
            plugin("jooq", "nu.studer.jooq").version(jooqPlugin)
        }
        create("testLibs") {
            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test-junit5").version(kotlin)
            library("ktor-server-tests", "io.ktor", "ktor-server-tests-jvm").version(ktor)
            library("mockk", "io.mockk", "mockk").version(mockk)
        }
    }
}
