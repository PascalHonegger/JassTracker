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

    apply(plugin = "kover")

    repositories {
        mavenCentral()
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
