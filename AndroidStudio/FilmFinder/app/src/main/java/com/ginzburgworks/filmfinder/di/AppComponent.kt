package com.ginzburgworks.filmfinder.di

import android.content.Context
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.di.modules.*
import com.ginzburgworks.filmfinder.view.fragments.DetailsFragment
import com.ginzburgworks.filmfinder.view.fragments.HomeFragment
import com.ginzburgworks.filmfinder.view.fragments.SettingsFragment
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import com.ginzburgworks.filmfinder.viewmodels.SettingsFragmentViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class,
        RemoteModule::class,
        RecyclerModule::class,
        DomainModule::class
    ]
)
interface AppComponent {

    fun injectApp (app: App)
    fun injectHomeVM(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectSettingsVM(settingsFragmentViewModel: SettingsFragmentViewModel)
}