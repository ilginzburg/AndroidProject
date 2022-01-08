package com.ginzburgworks.filmfinder.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

private const val DAY_MODE = false

class SettingsManager @Inject constructor(context: Context) {

    private var preferenceProvider = PreferenceProvider(context)
    var savedNightMode = preferenceProvider.getNightModeSetting()

    fun setSelectedNightMode(selectedNightMode: Boolean) {
        preferenceProvider.saveNightModeSetting(selectedNightMode)
        setUINightMode(selectedNightMode)
    }

    private fun setUINightMode(requestedNightMode: Boolean) {
        var appCompatDelegateMode = AppCompatDelegate.MODE_NIGHT_YES
        if (requestedNightMode == DAY_MODE)
            appCompatDelegateMode = AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(appCompatDelegateMode)
    }

    fun initSettings() {
        setUINightMode(savedNightMode)
    }

}

