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
    runtimeOnly(libs.postgresql)
    jooqGenerator(libs.postgresql)
    testImplementation(testLibs.kotlin.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
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
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isPojos = false
                        isFluentSetters = false
                    }
                    target.apply {
                        packageName = "dev.honegger.jasstracker.database"
                        directory = "src/main/java"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
