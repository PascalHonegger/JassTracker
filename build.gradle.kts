buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.versions)
}

allprojects {
    group = "dev.honegger"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

dependencies {
    kover(project(":Backend:bootstrap"))
    kover(project(":Backend:data-access"))
    kover(project(":Backend:domain"))
    kover(project(":Backend:web-api"))
    kover(project(":Backend:security"))
}

koverReport {
    filters {
        excludes {
            packages("dev.honegger.jasstracker.data.database", "dev.honegger.jasstracker.bootstrap.*")
        }
    }
    defaults {
        verify {
            rule {
                bound {
                    minValue = 90
                }
            }
        }
    }
}
