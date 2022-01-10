package com.ginzburgworks.filmfinder.di.modules

import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import com.ginzburgworks.filmfinder.viewmodels.CommonViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RecyclerModule {

    @Provides
    @Singleton
    fun provideRecycler() = FilmListRecyclerAdapter()
}


