package com.ginzburgworks.filmfinder

import android.app.Application
import com.ginzburgworks.filmfinder.data.SettingsManager
import com.ginzburgworks.filmfinder.di.AppComponent
import com.ginzburgworks.filmfinder.di.DaggerAppComponent

open class App : Application() {
    lateinit var appComponent: AppComponent
    private lateinit var settingsManager: SettingsManager

    override fun onCreate() {
        settingsManager = SettingsManager(this)
        settingsManager.initSettings()
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}