package com.ginzburgworks.filmfinder.di.modules


import android.content.Context
import androidx.room.Room
import com.ginzburgworks.filmfinder.data.local.db.FilmsLocalDataSource
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.db.dao.FilmDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DATA_BASE_NAME = "film_db"

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideFilmDao(context: Context) = Room.databaseBuilder(
        context, FilmsLocalDataSource::class.java, DATA_BASE_NAME
    ).build().filmDao()

    @Provides
    @Singleton
    fun provideRepository(filmDao: FilmDao) = FilmsRepository(filmDao)
}
