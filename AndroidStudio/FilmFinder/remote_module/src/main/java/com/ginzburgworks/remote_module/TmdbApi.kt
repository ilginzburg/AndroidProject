package com.ginzburgworks.remote_module

import com.ginzburgworks.remote_module.entity.TmdbResultsDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val CATEGORY_VAL = "category"
private const val API_KEY_VAL = "api_key"
private const val LANGUAGE_VAL = "language"
private const val PAGE_VAL = "page"
private const val QUERY_VAL = "query"
private const val INCLUDE_ADULT_VAL = "include_adult"

interface TmdbApi {

    @GET("3/movie/{category}")
    fun getFilms(
        @Path(CATEGORY_VAL) category: String,
        @Query(API_KEY_VAL) apiKey: String,
        @Query(LANGUAGE_VAL) language: String,
        @Query(PAGE_VAL) page: Int
    ): Single<TmdbResultsDto>

    @GET("3/search/movie")
    fun getSearchResult(
        @Query(API_KEY_VAL) apiKey: String,
        @Query(LANGUAGE_VAL) language: String,
        @Query(PAGE_VAL) page: Int,
        @Query(QUERY_VAL) searchQuery: String,
        @Query(INCLUDE_ADULT_VAL) adult: Boolean
    ): Single<TmdbResultsDto>
}




