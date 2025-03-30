plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(testLibs.plugins.kover)
}

application {
    mainClass.set("dev.honegger.jasstracker.bootstrap.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
        vendor.set(JvmVendorSpec.GRAAL_VM)
    }
}

dependencies {
    implementation(project(":Backend:data-access"))
    implementation(project(":Backend:domain"))
    implementation(project(":Backend:web-api"))
    implementation(project(":Backend:security"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.auto.head.response)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.logback)
    implementation(libs.kotlin.logging)
    implementation(libs.slf4j)
    implementation(libs.java.jwt)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        manifest {
            attributes("Main-Class" to "dev.honegger.jasstracker.bootstrap.ApplicationKt")
        }
    }
}
