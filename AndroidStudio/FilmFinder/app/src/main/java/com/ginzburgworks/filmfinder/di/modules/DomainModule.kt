package com.ginzburgworks.filmfinder.di.modules

import dagger.Binds
import dagger.Module
import javax.inject.Inject

@Module
abstract class DomainModule {
    @Binds
    abstract fun bindInteractor(interactorImpl: DomainImpl): DomainImpl
    class DomainImpl @Inject constructor()
}



