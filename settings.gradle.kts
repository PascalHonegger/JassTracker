rootProject.name = "JassTracker"
include(":Backend:bootstrap")
include(":Backend:data-access")
include(":Backend:domain")
include(":Backend:web-api")
include(":Backend:security")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("testLibs") {
            from(files("gradle/test-libs.versions.toml"))
        }
    }
}
