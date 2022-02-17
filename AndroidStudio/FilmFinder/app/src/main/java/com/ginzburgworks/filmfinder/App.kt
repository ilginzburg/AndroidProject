package com.ginzburgworks.filmfinder

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ginzburgworks.filmfinder.di.AppComponent
import com.ginzburgworks.filmfinder.di.DaggerAppComponent
import com.ginzburgworks.filmfinder.di.modules.DomainModule
import com.ginzburgworks.filmfinder.di.modules.RepositoryModule
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.remote_module.DaggerRemoteComponent
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var interactor: Interactor
    var nightModeSwitched = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        val remoteProvider = DaggerRemoteComponent.create()
        appComponent = DaggerAppComponent.builder().remoteProvider(remoteProvider)
            .repositoryModule(RepositoryModule()).domainModule(DomainModule(this)).build()
        instance.appComponent.injectApp(this)
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