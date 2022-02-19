package com.ginzburgworks.filmfinder.di.modules

import com.ginzburgworks.filmfinder.data.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.local_module.LocalRepository
import com.ginzburgworks.remote_module.TmdbApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun providePreferences() = PreferenceProvider()

    @Singleton
    @Provides
    fun provideInteractor(
        repository: LocalRepository, tmdbApi: TmdbApi, preferenceProvider: PreferenceProvider
    ) = Interactor(
        repo = repository, retrofitService = tmdbApi, preferenceProvider = preferenceProvider
    )
}