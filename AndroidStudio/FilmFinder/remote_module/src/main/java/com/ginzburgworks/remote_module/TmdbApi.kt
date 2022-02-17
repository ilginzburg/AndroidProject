package com.ginzburgworks.remote_module

import com.ginzburgworks.remote_module.entity.TmdbResultsDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("3/movie/{category}")
    fun getFilms(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Single<TmdbResultsDto>

    @GET("3/search/movie")
    fun getSearchResult(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") searchQuery: String,
        @Query("include_adult") adult: Boolean
    ): Single<TmdbResultsDto>
}




