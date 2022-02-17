package com.ginzburgworks.filmfinder.di

import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.di.modules.DomainModule
import com.ginzburgworks.filmfinder.di.modules.RecyclerModule
import com.ginzburgworks.filmfinder.di.modules.RepositoryModule
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import com.ginzburgworks.filmfinder.viewmodels.SettingsFragmentViewModel
import com.ginzburgworks.remote_module.RemoteProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RemoteProvider::class],
    modules = [RepositoryModule::class, RecyclerModule::class, DomainModule::class]
)
interface AppComponent {

    fun injectApp(app: App)
    fun injectHomeVM(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectSettingsVM(settingsFragmentViewModel: SettingsFragmentViewModel)
}