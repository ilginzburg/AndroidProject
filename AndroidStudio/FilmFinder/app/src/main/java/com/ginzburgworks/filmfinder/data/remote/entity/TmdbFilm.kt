package com.ginzburgworks.filmfinder.data.remote.entity

import com.google.gson.annotations.SerializedName

private const val ADULT_SERIALIZED_NAME = "adult"
private const val BACKDROP_PATH_SERIALIZED_NAME = "backdrop_path"
private const val GENRE_IDS_SERIALIZED_NAME = "genre_ids"
private const val ID_SERIALIZED_NAME = "id"
private const val ORIGINAL_LANGUAGE_SERIALIZED_NAME = "original_language"
private const val ORIGINAL_TITLE_SERIALIZED_NAME = "original_title"
private const val OVERVIEW_SERIALIZED_NAME = "overview"
private const val POPULARITY_SERIALIZED_NAME = "popularity"
private const val POSTER_PATH_SERIALIZED_NAME = "poster_path"
private const val RELEASE_DATE_SERIALIZED_NAME = "release_date"
private const val TITLE_SERIALIZED_NAME = "title"
private const val VIDEO_SERIALIZED_NAME = "video"
private const val VOTE_AVERAGE_SERIALIZED_NAME = "vote_average"
private const val VOTE_COUNT_SERIALIZED_NAME = "vote_count"

data class TmdbFilm(
    @SerializedName(ADULT_SERIALIZED_NAME) val adult: Boolean,
    @SerializedName(BACKDROP_PATH_SERIALIZED_NAME) val backdropPath: String,
    @SerializedName(GENRE_IDS_SERIALIZED_NAME) val genreIds: List<Int>,
    @SerializedName(ID_SERIALIZED_NAME) val id: Int,
    @SerializedName(ORIGINAL_LANGUAGE_SERIALIZED_NAME) val originalLanguage: String,
    @SerializedName(ORIGINAL_TITLE_SERIALIZED_NAME) val originalTitle: String,
    @SerializedName(OVERVIEW_SERIALIZED_NAME) val overview: String,
    @SerializedName(POPULARITY_SERIALIZED_NAME) val popularity: Double,
    @SerializedName(POSTER_PATH_SERIALIZED_NAME) val posterPath: String,
    @SerializedName(RELEASE_DATE_SERIALIZED_NAME) val releaseDate: String,
    @SerializedName(TITLE_SERIALIZED_NAME) val title: String,
    @SerializedName(VIDEO_SERIALIZED_NAME) val video: Boolean,
    @SerializedName(VOTE_AVERAGE_SERIALIZED_NAME) val voteAverage: Double,
    @SerializedName(VOTE_COUNT_SERIALIZED_NAME) val voteCount: Int
)