package com.ginzburgworks.filmfinder

import android.app.Application
import com.ginzburgworks.filmfinder.data.MainRepository
import com.ginzburgworks.filmfinder.domain.Interactor

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var interactor: Interactor

    override fun onCreate() {
        super.onCreate()
        instance = this
        repo = MainRepository()
        interactor = Interactor(repo)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}