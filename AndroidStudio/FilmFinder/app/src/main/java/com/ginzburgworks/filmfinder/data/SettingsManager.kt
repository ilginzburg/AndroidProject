package com.ginzburgworks.filmfinder.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.ginzburgworks.filmfinder.di.modules.Settings
import javax.inject.Inject

private const val DAY_MODE = false

class SettingsManager @Inject constructor(private val context: Context) : Settings {


private var pref = PreferenceProvider(context)


    fun setSelectedNightMode(selectedNightMode: Boolean) {
        nightModeState = selectedNightMode
        setUINightMode()
        pref.saveSettings(selectedNightMode)
    }


    private fun setUINightMode() {
        var appCompatDelegateMode = AppCompatDelegate.MODE_NIGHT_YES
        if (nightModeState == DAY_MODE) appCompatDelegateMode = AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(appCompatDelegateMode)
    }

    fun initSettings() {
        pref.readSettings()
        setUINightMode()
    }

    companion object {
        var nightModeState = DAY_MODE
    }

}

