plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kover)
}

kotlin {
    jvmToolchain(20)
}

dependencies {
    implementation(libs.kotlin.logging)
    implementation(libs.kotlinx.datetime)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
