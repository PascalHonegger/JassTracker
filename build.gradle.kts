import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

@Suppress("DSL_SCOPE_VIOLATION") // see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kover)
}

allprojects {
    group = "dev.honegger"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }

        val javaLauncher = project.extensions.getByType<JavaToolchainService>().launcherFor {
            languageVersion.set(JavaLanguageVersion.of("17"))
        }
        kotlinJavaToolchain.toolchain.use(javaLauncher)
    }
}

val koverExcludes = listOf("dev.honegger.jasstracker.data.database.*", "dev.honegger.jasstracker.bootstrap.*")
tasks.koverMergedHtmlReport {
    isEnabled = true
    excludes = koverExcludes
}

tasks.koverMergedXmlReport {
    isEnabled = false
}

tasks.koverMergedVerify {
    excludes = koverExcludes
    rule {
        name = "Minimal line coverage rate in percent"
        bound {
            minValue = 40 // TODO slowly increase to reach 80% coverage
        }
    }
}
