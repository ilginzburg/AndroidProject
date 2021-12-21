package com.ginzburgworks.filmfinder.di.modules

import androidx.databinding.ktx.BuildConfig
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

    @Binds
    abstract fun bindOkHttpClient(okHttpClientCreator: OkHttpClientCreator): Remote

    @Binds
    abstract fun bindRetrofit(retrofitCreator: RetrofitCreator): Remote

    @Binds
    abstract fun bindTmdbApi(apiImpl: TmdbApiImpl): TmdbApi

}


class OkHttpClientCreator @Inject constructor() : Remote {

    fun okHttpClientImpl(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
    }
}

class RetrofitCreator @Inject constructor() : Remote {

    @Inject
    lateinit var okHttpClientCreator: OkHttpClientCreator

    fun retrofitImpl(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientCreator.okHttpClientImpl())
            .build()
    }
}


class TmdbApiImpl @Inject constructor() : TmdbApi, Remote {

    @Inject
    lateinit var retrofitCreator: RetrofitCreator

    override fun getFilms(category: String,apiKey: String, language: String, page: Int): Call<TmdbResultsDto> {
        return retrofitCreator.retrofitImpl().create(TmdbApi::class.java)
            .getFilms(category,apiKey, language, page)
    }
}

interface Remote