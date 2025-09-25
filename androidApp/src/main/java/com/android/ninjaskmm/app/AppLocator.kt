// androidApp/src/main/java/com/android/ninjaskmm/android/AppLocator.kt
package com.android.ninjaskmm.app

import android.content.Context
import com.android.ninjaskmm.android.BuildConfig
import com.android.repository.AnimalsRepository
import com.android.repository.createAnimalsRepository   // <-- use the shared factory
import com.russhwolf.settings.SharedPreferencesSettings

object AppLocator {
    fun animalsRepository(context: Context): AnimalsRepository {
        val prefs = context.getSharedPreferences("animals_cache", Context.MODE_PRIVATE)
        val settings = SharedPreferencesSettings(prefs)
        return createAnimalsRepository(settings, BuildConfig.API_NINJAS_KEY)
    }
}
