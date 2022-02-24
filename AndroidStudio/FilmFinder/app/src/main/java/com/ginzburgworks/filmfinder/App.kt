package com.ginzburgworks.filmfinder

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ginzburgworks.filmfinder.data.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.di.AppComponent
import com.ginzburgworks.filmfinder.di.DaggerAppComponent
import com.ginzburgworks.filmfinder.di.modules.DomainModule
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.local_module.RepositoryModule
import com.ginzburgworks.remote_module.DaggerRemoteComponent
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var interactor: Interactor
    var nightModeSwitched = false
    var nightModeIsOnBecauseOfLowBattery = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        val remoteProvider = DaggerRemoteComponent.create()

        appComponent = DaggerAppComponent.builder().remoteProvider(remoteProvider).repositoryModule(
            RepositoryModule(this)
        ).domainModule(DomainModule()).build()
        instance.appComponent.injectApp(this)
        initUINightMode()
    }

    fun switchToNightMode() {
        setUINightMode(PreferenceProvider.NIGHT_MODE)
        nightModeIsOnBecauseOfLowBattery = true
    }

    fun switchToDayMode() {
        setUINightMode(PreferenceProvider.DAY_MODE)
    }

    fun setUINightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        saveNightMode(mode)
    }

    private fun saveNightMode(mode: Int) {
        interactor.saveNightMode(mode)
    }

    private fun initUINightMode() {
        setUINightMode(interactor.getNightMode())
    }

    companion object {
        lateinit var instance: App
            private set
    }
}