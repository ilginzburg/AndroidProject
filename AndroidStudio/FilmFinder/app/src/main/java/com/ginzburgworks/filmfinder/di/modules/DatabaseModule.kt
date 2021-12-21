package com.ginzburgworks.filmfinder.di.modules

import android.content.Context
import com.ginzburgworks.filmfinder.data.db.DatabaseHelper
import com.ginzburgworks.filmfinder.data.db.MainRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Singleton
    @Provides
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}