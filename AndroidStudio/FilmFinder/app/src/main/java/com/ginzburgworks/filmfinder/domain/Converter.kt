package com.ginzburgworks.filmfinder.domain

import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.remote.entity.TmdbFilm

object Converter {
    fun convertTmdbFilmListToFilmList(list: List<TmdbFilm>?, page: Int?, currentCategory: String): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    category = currentCategory,
                    page = page?: DefaultFilm.film.page,
                    title = it.title ?: DefaultFilm.film.title,
                    poster = it.posterPath ?: DefaultFilm.film.poster,
                    description = it.overview ?: DefaultFilm.film.description,
                    rating = it.voteAverage ?: DefaultFilm.film.rating,
                    isInFavorites = false
                )
            )
        }
        return result
    }


    object DefaultFilm {
        private const val default_id = 1
        private const val default_page = 1
        private const val default_category = "popular"
        private const val default_title = "defaultTitle"
        private const val default_poster = "R.drawable.tv_default"
        private const val default_description = "descriptionDefault"
        private const val default_rating = 1.1
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