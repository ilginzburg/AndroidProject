package com.ginzburgworks.filmfinder.domain

import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.remote.entity.TmdbFilm


private const val ID_DEFAULT_VALUE = 1
private const val PAGE_DEFAULT_VALUE = 1
private const val CATEGORY_DEFAULT_VALUE = "popular"
private const val TITLE_DEFAULT_VALUE = "defaultTitle"
private const val POSTER_DEFAULT_VALUE = "R.drawable.tv_default"
private const val DESCRIPTION_DEFAULT_VALUE = "descriptionDefault"
private const val RATING_DEFAULT_VALUE = 1.1
private const val IS_IN_FAVORITES_INITIAL_VALUE = false

object Converter {
    fun convertTmdbFilmListToFilmList(
        list: List<TmdbFilm>?,
        page: Int?,
        currentCategory: String
    ): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    category = currentCategory,
                    page = page ?: DefaultFilm.film.page,
                    title = it.title ?: DefaultFilm.film.title,
                    poster = it.posterPath ?: DefaultFilm.film.poster,
                    description = it.overview ?: DefaultFilm.film.description,
                    rating = it.voteAverage ?: DefaultFilm.film.rating,
                    isInFavorites = IS_IN_FAVORITES_INITIAL_VALUE
                )
            )
        }
        return result
    }


    object DefaultFilm {
        private const val default_id = ID_DEFAULT_VALUE
        private const val default_page = PAGE_DEFAULT_VALUE
        private const val default_category = CATEGORY_DEFAULT_VALUE
        private const val default_title = TITLE_DEFAULT_VALUE
        private const val default_poster = POSTER_DEFAULT_VALUE
        private const val default_description = DESCRIPTION_DEFAULT_VALUE
        private const val default_rating = RATING_DEFAULT_VALUE
        val film = Film(
            default_id,
            default_page,
            default_category,
            default_title,
            default_poster,
            default_description,
            default_rating
        )
    }
}