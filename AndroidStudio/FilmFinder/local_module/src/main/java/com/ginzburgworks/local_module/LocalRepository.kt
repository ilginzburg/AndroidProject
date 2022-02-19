package com.ginzburgworks.local_module

import com.ginzburgworks.local_module.dao.FilmDao
import io.reactivex.rxjava3.core.Observable

class LocalRepository(private val filmDao: FilmDao) {

    fun putPageOfFilms(pageOfFilms: List<Film>) {
        filmDao.insertAll(pageOfFilms)
    }

    fun getFilmsInCategory(category: String): Observable<List<Film>> {
        return filmDao.getCachedFilmsByCategory(category)
    }

    fun deleteAll() = filmDao.deleteAll()
}