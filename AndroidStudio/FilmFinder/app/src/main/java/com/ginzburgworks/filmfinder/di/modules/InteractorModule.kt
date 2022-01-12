package com.ginzburgworks.filmfinder.di.modules

import android.content.Context
import dagger.Binds
import dagger.Module
import javax.inject.Inject

@Module
abstract class InteractorModule(val context: Context) {

    @Binds
    abstract fun bindInteractor(interactorImpl: InteractorImpl): InteractorImpl
    class InteractorImpl @Inject constructor()
}




