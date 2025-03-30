plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(testLibs.plugins.kover)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
        vendor.set(JvmVendorSpec.GRAAL_VM)
    }
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
