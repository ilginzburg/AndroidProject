package com.ginzburgworks.filmfinder.di.modules

import com.ginzburgworks.filmfinder.BuildConfig
import com.ginzburgworks.filmfinder.data.ApiConstants
import com.ginzburgworks.filmfinder.data.TmdbApi
import com.ginzburgworks.filmfinder.data.entity.TmdbResultsDto
import dagger.Binds
import dagger.Module
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Module
abstract class RemoteModule {

    //@Binds
    abstract fun provideOkHttpClient(okHttpClientImpl: OkHttpClient): OkHttpClient


    //@Binds
    abstract fun provideRetrofit(retrofitImpl: Retrofit): Retrofit



    @Binds
    abstract fun bindTmdbApi(apiImpl: TmdbApiImpl): TmdbApi

}

class TmdbApiImpl @Inject constructor() : TmdbApi {

    private val okHttpClientImpl = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    private val retrofitImpl = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClientImpl)
        .build()


    override fun getFilms(apiKey: String, language: String, page: Int): Call<TmdbResultsDto> {
        return  retrofitImpl.create(TmdbApi::class.java).getFilms(apiKey,language,page)
    }

}