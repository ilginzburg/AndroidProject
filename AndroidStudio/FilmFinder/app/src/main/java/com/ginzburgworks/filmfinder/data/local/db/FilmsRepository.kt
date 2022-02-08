package com.ginzburgworks.filmfinder.data.local.db

import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.dao.FilmDao
import io.reactivex.rxjava3.core.Observable

class FilmsRepository(private val filmDao: FilmDao) {

    fun putPageOfFilms(pageOfFilms: List<Film>) = filmDao.insertAll(pageOfFilms)

    fun getPageOfFilmsInCategory(page: Int, category: String): Observable<List<Film>> {
        return filmDao.getCachedFilmsByPageAndCategory(page, category)
    }

    fun deleteAll() = filmDao.deleteAll()
}