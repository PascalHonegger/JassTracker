buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(testLibs.plugins.kover)
    alias(testLibs.plugins.versions)
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

kover {
    reports {
        filters {
            excludes {
                packages("dev.honegger.jasstracker.data.database", "dev.honegger.jasstracker.bootstrap", "dev.honegger.jasstracker.bootstrap.plugins")
            }
        }
        verify {
            rule {
                bound {
                    minValue = 90
                }
            }
        }
    }
}
