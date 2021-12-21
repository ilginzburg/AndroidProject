package com.ginzburgworks.filmfinder.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject


open class PreferenceProvider @Inject constructor(context: Context) {
    private val appContext = context.applicationContext
    private val preference = appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    init {
        if (preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit { putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) }
            preference.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
        }
    }

    fun saveDefaultCategory(category: String) {
        preference.edit { putString(KEY_DEFAULT_CATEGORY, category) }
    }

    fun getDefaultCategory(): String {
        return preference.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
    }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener){
        preference.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unRegisterListener(listener:SharedPreferences.OnSharedPreferenceChangeListener){
        preference.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun readSettings() {
        SettingsManager.nightModeState = preference.getBoolean(
            KEY_NIGHT_MODE_STATE,
            DEFAULT_MODE
        )
    }

    fun saveSettings(mode: Boolean) {
        preference.edit().putBoolean(KEY_NIGHT_MODE_STATE, mode).apply()
    }

    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val DEFAULT_CATEGORY = "popular"
        private const val PREFERENCE_NAME = "settings"
        private const val KEY_NIGHT_MODE_STATE = "night_mode_key"
        private const val NIGHT_MODE = true
        private const val DEFAULT_MODE = NIGHT_MODE
    }
}