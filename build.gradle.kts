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

    tasks.withType<JavaCompile>().configureEach {
        val service = project.extensions.getByType<JavaToolchainService>()
        val customProvider = service.compilerFor {
            languageVersion.set(JavaLanguageVersion.of("20"))
        }
        javaCompiler.set(customProvider)
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.UsesKotlinJavaToolchain>().configureEach {
        val service = project.extensions.getByType<JavaToolchainService>()
        val customLauncher = service.launcherFor {
            languageVersion.set(JavaLanguageVersion.of("20"))
        }
        kotlinJavaToolchain.toolchain.use(customLauncher)
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
