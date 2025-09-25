plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    jvmToolchain(17)

    // Android
    androidTarget()

    // iOS targets
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // --- Ktor / KotlinX ---
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)

                // --- Multiplatform Settings (common API only) ---
                implementation(libs.multiplatform.settings)   // ✅ keep
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)

                // If your shared code actually uses these on Android, keep them.
                implementation(libs.androidx.lifecycle.viewmodel.ktx)
                implementation(libs.androidx.lifecycle.viewmodel.compose)

                // ❌ REMOVE this line to avoid duplicate classes
                // implementation(libs.multiplatform.settings.android)
            }
        }

        // Aggregate iOS source set
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
                // No Settings platform dep needed here
            }
        }
        iosX64Main.dependsOn(iosMain)
        iosArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Main.dependsOn(iosMain)
    }
}

android {
    namespace = "com.android.ninjaskmm.shared"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        targetSdk = 36
    }
    buildFeatures { compose = false }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
