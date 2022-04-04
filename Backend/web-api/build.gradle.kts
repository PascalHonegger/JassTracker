@Suppress("DSL_SCOPE_VIOLATION") // see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":Backend:domain"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(testLibs.ktor.server.tests)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
