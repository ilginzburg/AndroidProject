package com.ginzburgworks.filmfinder.di

import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.di.modules.DomainModule
import com.ginzburgworks.filmfinder.di.modules.RecyclerModule
import com.ginzburgworks.filmfinder.di.modules.RemoteModule
import com.ginzburgworks.filmfinder.di.modules.RepositoryModule
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import com.ginzburgworks.filmfinder.viewmodels.SettingsFragmentViewModel
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

    @Component.Factory
    interface Factory {
        fun create(domain: DomainModule, repo: RepositoryModule, remote: RemoteModule): AppComponent
    }

    fun injectApp(app: App)
    fun injectHomeVM(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectSettingsVM(settingsFragmentViewModel: SettingsFragmentViewModel)
}