plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(testLibs.plugins.kover)
}

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation(project(":Backend:domain"))
    implementation(libs.java.jwt)
    implementation(libs.argon2.jvm.nolibs)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.argon2.jvm)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
