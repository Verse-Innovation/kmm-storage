plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.tagd.arch)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":libraries:storage-core:storage-core-test"))
                api(libs.tagd.arch.test)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.gson)
                api(libs.tagd.android)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.tagd.android)
            }
        }
    }
}

android {
    namespace = "io.verse.storage.core"
}

pomBuilder {
    description.set("Storage core library")
}