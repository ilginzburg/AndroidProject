package com.ginzburgworks.filmfinder

import android.app.Application
import com.ginzburgworks.filmfinder.di.AppComponent
import com.ginzburgworks.filmfinder.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}