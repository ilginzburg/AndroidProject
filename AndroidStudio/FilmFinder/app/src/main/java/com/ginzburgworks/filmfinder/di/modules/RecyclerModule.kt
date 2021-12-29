package com.ginzburgworks.filmfinder.di.modules

import com.ginzburgworks.filmfinder.data.db.MainRepository
import com.ginzburgworks.filmfinder.data.db.dao.FilmDao
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RecyclerModule {

    @Provides
    @Singleton
    fun provideRecycler() = FilmListRecyclerAdapter()
}