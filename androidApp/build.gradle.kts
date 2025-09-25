// androidApp/build.gradle.kts
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler) // Kotlin 2.2.20
}

/* --- Load API key from multiple sources --- */
fun loadProp(file: java.io.File, name: String): String? =
    if (file.exists()) Properties().apply { file.inputStream().use { load(it) } }.getProperty(name) else null

val keyFromGradleProp = providers.gradleProperty("API_NINJAS_KEY").orNull
val keyFromEnv        = providers.environmentVariable("API_NINJAS_KEY").orNull
val keyFromModuleLP   = loadProp(project.file("local.properties"), "API_NINJAS_KEY")
val keyFromRootLP     = loadProp(rootProject.file("local.properties"), "API_NINJAS_KEY")

val apiNinjasKey = sequenceOf(keyFromGradleProp, keyFromEnv, keyFromModuleLP, keyFromRootLP)
    .firstOrNull { !it.isNullOrBlank() }
    ?.trim()
    ?: ""

if (apiNinjasKey.isBlank()) {
    logger.warn(
        """
        ⚠️ API_NINJAS_KEY is missing. Build will proceed with an empty key (debug).
           To fix, set one of:
             • ${project.projectDir}/local.properties -> API_NINJAS_KEY=your_key
             • ${rootProject.projectDir}/local.properties -> API_NINJAS_KEY=your_key
             • gradle.properties (project or user) -> API_NINJAS_KEY=your_key
             • or export:  export API_NINJAS_KEY=your_key
        """.trimIndent()
    )
}

android {
    namespace = "com.android.ninjaskmm.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.android.ninjaskmm.android"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_NINJAS_KEY", "\"$apiNinjasKey\"")
        manifestPlaceholders["API_NINJAS_KEY"] = apiNinjasKey
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Using Kotlin Compose plugin → no composeOptions block needed
    // composeOptions { kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get() }

    packaging {
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_NINJAS_KEY", "\"$apiNinjasKey\"")
            manifestPlaceholders["API_NINJAS_KEY"] = apiNinjasKey
        }
        release {
            isMinifyEnabled = false
            if (apiNinjasKey.isBlank()) {
                throw GradleException("API_NINJAS_KEY is required for release builds. Add it to local.properties or gradle.properties.")
            }
            buildConfigField("String", "API_NINJAS_KEY", "\"$apiNinjasKey\"")
            manifestPlaceholders["API_NINJAS_KEY"] = apiNinjasKey
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

/**
 * Safety net: if any module accidentally brings in the debug variant
 * of Multiplatform Settings' Android artifact, exclude it here.
 */
configurations.all {
    exclude(group = "com.russhwolf", module = "multiplatform-settings-android-debug")
}

dependencies {
    // Compose BOM first
    implementation(platform(libs.compose.bom))

    implementation(project(":shared"))

    // Compose core ui/material
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)

    // Activity + Compose
    implementation(libs.androidx.activity.compose)

    // AndroidX core/lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Multiplatform Settings (Android impl for SharedPreferencesSettings) — only here
    implementation(libs.multiplatform.settings.android)

    // Tooling (debug only)
    debugImplementation(libs.compose.ui.tooling)
}
