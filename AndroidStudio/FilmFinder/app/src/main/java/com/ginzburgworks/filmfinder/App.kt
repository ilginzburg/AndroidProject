package com.ginzburgworks.filmfinder

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ginzburgworks.filmfinder.di.AppComponent
import com.ginzburgworks.filmfinder.di.DaggerAppComponent
import com.ginzburgworks.filmfinder.di.modules.DomainModule
import com.ginzburgworks.filmfinder.di.modules.RemoteModule
import com.ginzburgworks.filmfinder.di.modules.RepositoryModule
import com.ginzburgworks.filmfinder.domain.Interactor
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var interactor: Interactor
    var nightModeSwitched = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .repositoryModule(RepositoryModule())
            .domainModule(DomainModule(this))
            .build()
        App.instance.appComponent.injectApp(this)
        initUINightMode()
    }

    private fun initUINightMode() {
        setUINightMode(interactor.getNightMode())
    }

    fun setUINightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}