package com.ginzburgworks.local_module


import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DATA_BASE_NAME = "film_db"

@Module
class RepositoryModule(val context: Context) {

    @Singleton
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideFilmDao(context: Context) = Room.databaseBuilder(
        context, FilmsLocalDataSource::class.java, DATA_BASE_NAME
    ).build().filmDao()

    @Singleton
    @Provides
    fun provideRepository(filmDao: com.ginzburgworks.local_module.dao.FilmDao) =
        LocalRepository(filmDao)
}
