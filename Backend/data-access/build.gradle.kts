import org.jooq.meta.jaxb.ForcedType

@Suppress("DSL_SCOPE_VIOLATION") // see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jooq)
}

dependencies {
    implementation(project(":Backend:domain"))
    implementation(libs.logback)
    implementation(libs.flyway)
    implementation(libs.jooq)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.reflect)
    runtimeOnly(libs.postgresql)
    jooqGenerator(libs.postgresql)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.testcontainers)
    testImplementation(testLibs.testcontainers.junit)
    testImplementation(testLibs.testcontainers.postgresql)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of("18"))
    }
}

jooq {
    version.set(libs.versions.jooq.get())
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("jasstracker") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/jasstracker"
                    user = "jasstracker"
                    password = "password"
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"


                        // includes = "jasstracker.*"

                        // All elements that are excluded from your schema (A Java regular expression.
                        // Use the pipe to separate several expressions). Excludes match before
                        // includes, i.e. excludes have a higher priority
                        excludes = """
                            flyway_schema_history         # This table (unqualified name) should not be generated‡
                        """

                        forcedTypes = listOf(
                            ForcedType().apply {
                                userType = "kotlinx.datetime.LocalDateTime"

                                converter = "dev.honegger.jasstracker.data.converters.LocalDateTimeConverter"

                                includeTypes = "TIMESTAMP"
                            }
                        )
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isPojos = false
                        isFluentSetters = false
                    }
                    target.apply {
                        packageName = "dev.honegger.jasstracker.data.database"
                        directory = "src/main/java"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
