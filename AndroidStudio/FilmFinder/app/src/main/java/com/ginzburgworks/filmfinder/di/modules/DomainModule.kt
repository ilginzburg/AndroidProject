package com.ginzburgworks.filmfinder.di.modules

import android.content.Context
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.data.remote.TmdbApi
import com.ginzburgworks.filmfinder.domain.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {

    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractor(
        repository: FilmsRepository, tmdbApi: TmdbApi, preferenceProvider: PreferenceProvider
    ) = Interactor(
        repo = repository, retrofitService = tmdbApi, preferenceProvider = preferenceProvider
    )
}