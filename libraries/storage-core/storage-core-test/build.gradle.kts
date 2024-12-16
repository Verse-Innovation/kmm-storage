plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":libraries:storage-core"))
            }
        }
    }
}

android {
    namespace = "io.verse.storage.core.test"
}

pomBuilder {
    description.set("Storage core test library")
}