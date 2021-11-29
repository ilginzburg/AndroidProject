package com.ginzburgworks.filmfinder

import android.app.Application
import com.ginzburgworks.filmfinder.data.ApiConstants
import com.ginzburgworks.filmfinder.data.TmdbApi
import com.ginzburgworks.filmfinder.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var interactor: Interactor

    override fun onCreate() {
        super.onCreate()
        instance = this
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val retrofitService = retrofit.create(TmdbApi::class.java)
        interactor = Interactor(retrofitService)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}