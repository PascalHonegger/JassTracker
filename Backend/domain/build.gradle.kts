plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(testLibs.plugins.kover)
}

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation(libs.kotlin.logging)
    implementation(libs.slf4j)
    implementation(libs.kotlinx.datetime)
    testImplementation(testLibs.kotlin.test)
    testImplementation(testLibs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class) {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}
