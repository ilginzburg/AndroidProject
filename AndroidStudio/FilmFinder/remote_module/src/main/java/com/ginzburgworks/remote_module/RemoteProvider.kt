package com.ginzburgworks.remote_module

interface RemoteProvider {
    fun provideRemote(): TmdbApi
}