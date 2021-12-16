package com.ginzburgworks.filmfinder.di.modules

import com.ginzburgworks.filmfinder.data.SettingsManager
import dagger.Binds
import dagger.Module

@Module
abstract class SettingsModule {

    @Binds
    abstract fun bindSettings(SettingsManagerObj: SettingsManager): Settings

}

interface Settings