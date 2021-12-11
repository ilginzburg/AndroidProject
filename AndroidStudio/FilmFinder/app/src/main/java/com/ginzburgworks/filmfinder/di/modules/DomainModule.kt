package com.ginzburgworks.filmfinder.di.modules

import com.ginzburgworks.filmfinder.data.TmdbApi
import com.ginzburgworks.filmfinder.domain.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideInteractor(tmdbApi: TmdbApi) = Interactor(retrofitService = tmdbApi)
}