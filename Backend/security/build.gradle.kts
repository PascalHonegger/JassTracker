plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(20)
}

dependencies {
    implementation(project(":Backend:domain"))
    implementation(libs.java.jwt)
    implementation(libs.argon2.jvm)
    implementation(libs.kotlinx.datetime)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
