pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "storage"
include(":applications:the101:android")
include(":libraries:storage-core")
include(":libraries:storage-core:storage-core-test")
