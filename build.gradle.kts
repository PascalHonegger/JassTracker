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
}

allprojects {
    group = "dev.honegger"
    version = "0.0.1"

    repositories {
        mavenCentral()
        // TODO remove once Ktor 2.0 is stable
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
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
