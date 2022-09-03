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
    alias(libs.plugins.versions)
}

allprojects {
    group = "dev.honegger"
    version = "0.0.1"

    apply(plugin = "kover")

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "19"
        }

        val javaLauncher = project.extensions.getByType<JavaToolchainService>().launcherFor {
            languageVersion.set(JavaLanguageVersion.of("19"))
        }
        kotlinJavaToolchain.toolchain.use(javaLauncher)
    }
}

koverMerged {
    enable()
    htmlReport {

    }
    xmlReport {

    }
    verify {
        rule {
            name = "Minimal line coverage rate in percent"
            bound {
                minValue = 90
            }
        }
    }

    filters {
        classes {
            excludes.add("dev.honegger.jasstracker.data.database.*")
            excludes.add("dev.honegger.jasstracker.bootstrap.*")
        }
    }
}
