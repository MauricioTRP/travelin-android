pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Mapbox Maven repository
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "travelin-android"
include(":app")
include(":auth:data")
include(":auth:domain")
include(":core")
include(":auth:presentation")
include(":core:presentation")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:database")
include(":core:domain")
include(":core:data")
include(":booking:data")
include(":booking:domain")
include(":booking:presentation")
include(":navigation")
include(":feature:onboarding")
include(":feature:onboarding:presentation")
include(":feature:onboarding:domain")
include(":feature:onboarding:data")
