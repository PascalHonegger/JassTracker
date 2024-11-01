plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
}

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation(project(":Backend:domain"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback)
    implementation(libs.java.jwt)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.ktor.server.content.negotiation)
    testImplementation(testLibs.ktor.server.tests)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
