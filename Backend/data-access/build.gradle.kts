@Suppress("DSL_SCOPE_VIOLATION") // see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":Backend:domain"))
    implementation(libs.logback)
    implementation(libs.flyway)
    runtimeOnly(libs.postgresql)
    testImplementation(testLibs.kotlin.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}