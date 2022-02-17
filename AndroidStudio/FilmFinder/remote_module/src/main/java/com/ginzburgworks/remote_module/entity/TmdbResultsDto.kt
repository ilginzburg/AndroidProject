package com.ginzburgworks.remote_module.entity

import com.google.gson.annotations.SerializedName

private const val PAGE_SERIALIZED_NAME = "page"
private const val RESULTS_SERIALIZED_NAME = "results"
private const val TOTAL_PAGES_SERIALIZED_NAME = "total_pages"
private const val TOTAL_RESULTS_SERIALIZED_NAME = "total_results"

data class TmdbResultsDto(
    @SerializedName(PAGE_SERIALIZED_NAME)
    val page: Int,
    @SerializedName(RESULTS_SERIALIZED_NAME)
    val tmdbFilms: List<TmdbFilm>,
    @SerializedName(TOTAL_PAGES_SERIALIZED_NAME)
    val totalPages: Int,
    @SerializedName(TOTAL_RESULTS_SERIALIZED_NAME)
    val totalResults: Int
)