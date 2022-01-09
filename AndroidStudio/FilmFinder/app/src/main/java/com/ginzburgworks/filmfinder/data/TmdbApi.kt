package com.ginzburgworks.filmfinder.data

import com.ginzburgworks.filmfinder.data.entity.TmdbResultsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TmdbApi {
    @GET("3/movie/{category}")
    suspend fun getFilms(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): TmdbResultsDto
}