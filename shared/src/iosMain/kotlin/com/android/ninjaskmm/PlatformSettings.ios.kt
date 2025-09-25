package com.android.ninjaskmm

import com.russhwolf.settings.Settings
import com.russhwolf.settings.NSUserDefaultsSettings

/**
 * iOS actual implementation that returns a Settings backed by NSUserDefaults.
 * The `name` parameter is passed through to the factory (useful if you want
 * separate suites; pass an empty string or a specific suite name as desired).
 */
actual fun provideSettings(name: String): Settings {
    // NSUserDefaultsSettings.Factory().create(name) will create a named suite if supported
    // Passing an empty string "" will use standard/default user defaults.
    return NSUserDefaultsSettings.Factory().create(name)
}
