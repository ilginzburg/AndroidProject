package com.ginzburgworks.filmfinder.di

import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.di.modules.DomainModule
import com.ginzburgworks.filmfinder.di.modules.RecyclerModule
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import com.ginzburgworks.filmfinder.viewmodels.SettingsFragmentViewModel
import com.ginzburgworks.local_module.RepositoryModule
import com.ginzburgworks.remote_module.RemoteProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RemoteProvider::class],
    modules = [RecyclerModule::class, DomainModule::class, RepositoryModule::class]
)
interface AppComponent {

    fun injectApp(app: App)
    fun injectHomeVM(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectSettingsVM(settingsFragmentViewModel: SettingsFragmentViewModel)
}