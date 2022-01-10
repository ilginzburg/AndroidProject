package com.ginzburgworks.filmfinder.data.local.shared

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.domain.PagesController
import javax.inject.Inject

private const val DEFAULT_MODE = PreferenceProvider.NIGHT_MODE
private const val KEY_LAST_BD_UPDATE_TIME = "last_bd_update_time_key"
private const val DEFAULT_BD_UPDATE_TIME = 0L
private const val KEY_TOTAL_PAGES_NUMBER = "total_pages_number_key"
private const val KEY_FIRST_LAUNCH = "first_launch"
const val KEY_FILMS_CATEGORY = "films_category_key"
private  val DEFAULT_CATEGORY = App.instance.getString(R.string.popular_category)
private const val PREFERENCE_NAME = "settings"
private const val KEY_NIGHT_MODE_STATE = "night_state_key"


open class PreferenceProvider @Inject constructor(context: Context) {
    private val appContext = context.applicationContext
    private val preference = appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    init {
        if (preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit { putString(KEY_FILMS_CATEGORY, DEFAULT_CATEGORY) }
            preference.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
        }
    }

    fun saveFilmsCategory(category: String) {
        preference.edit { putString(KEY_FILMS_CATEGORY, category) }
    }

    fun getFilmsCategory(): String {
        return preference.getString(KEY_FILMS_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
    }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preference.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unRegisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preference.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun getNightModeSetting(): Int {
        return preference.getInt(
            KEY_NIGHT_MODE_STATE,
            DEFAULT_MODE
        )
    }

    fun saveNightModeSetting(mode: Int) {
        preference.edit().putInt(KEY_NIGHT_MODE_STATE, mode).apply()
    }

    fun getBDUpdateTime(): Long {
        return preference.getLong(
            KEY_LAST_BD_UPDATE_TIME, DEFAULT_BD_UPDATE_TIME
        )
    }

    fun saveUpdateDbTime(lasBDUpdateTime: Long) {
        preference.edit().putLong(KEY_LAST_BD_UPDATE_TIME, lasBDUpdateTime).apply()
    }

    fun saveTotalPagesNumber(totalPagesNumber: Int, category: String) {
        preference.edit().putInt(KEY_TOTAL_PAGES_NUMBER + category, totalPagesNumber).apply()
    }

    fun getTotalPagesNumber(category: String): Int {
        return preference.getInt(
            KEY_TOTAL_PAGES_NUMBER + category,
            PagesController.getDefaultTotalPagesByCategory(category)
        )
    }

    companion object {
        const val NIGHT_MODE = 2
        const val DAY_MODE = 1
    }
}