// settings.gradle.kts

pluginManagement {
    repositories {
        // Order is not critical, but this is a common, reliable order
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // Kotlin (2.2.20) + Compose plugin
        id("org.jetbrains.kotlin.android") version "2.2.20"
        id("org.jetbrains.kotlin.multiplatform") version "2.2.20"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20"
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"

        // Android Gradle Plugin (AGP)
        id("com.android.application") version "8.10.1"
        id("com.android.library") version "8.10.1"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // gradlePluginPortal() here is optional; pluginManagement above already covers plugins
    }
}

rootProject.name = "NinjasKMM"
include(":shared", ":androidApp")
