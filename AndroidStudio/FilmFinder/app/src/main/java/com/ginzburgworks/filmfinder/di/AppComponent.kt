package com.ginzburgworks.filmfinder.di

import android.content.Context
import com.ginzburgworks.filmfinder.di.modules.*
import com.ginzburgworks.filmfinder.view.fragments.HomeFragment
import com.ginzburgworks.filmfinder.view.fragments.SettingsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DomainModule::class,
        DatabaseModule::class,
        RemoteModule::class,
        SettingsModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance  context: Context): AppComponent
    }

    fun inject(homeFragment: HomeFragment)
    fun inject(settingsFragment: SettingsFragment)
}