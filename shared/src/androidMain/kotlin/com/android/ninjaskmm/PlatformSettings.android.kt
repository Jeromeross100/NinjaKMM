package com.android.ninjaskmm

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

private lateinit var appContext: Context

/** Call once from your Android Application.onCreate() or Activity before using provideSettings() */
fun initSettings(context: Context) {
    appContext = context.applicationContext
}

/**
 * Provide a Settings instance backed by SharedPreferencesSettings (SharedPreferences under the hood).
 * This is the Android actual implementation for the common expect function.
 */
actual fun provideSettings(name: String): Settings {
    check(::appContext.isInitialized) {
        "Call initSettings(context) in your Application before using provideSettings()"
    }

    // Option A (preferred): use the factory which creates a named SharedPreferences suite if supported
    return SharedPreferencesSettings.Factory(appContext).create(name)

    // Option B (alternative): create directly from an Android SharedPreferences instance:
    // val prefs = appContext.getSharedPreferences(name, Context.MODE_PRIVATE)
    // return SharedPreferencesSettings(prefs)
}
