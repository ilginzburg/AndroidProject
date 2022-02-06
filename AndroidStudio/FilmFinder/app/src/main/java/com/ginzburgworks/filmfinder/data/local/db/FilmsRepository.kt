package com.ginzburgworks.filmfinder.data.local.db

import androidx.lifecycle.LiveData
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.dao.FilmDao
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class FilmsRepository(private val filmDao: FilmDao) {

    fun putPageOfFilms(pageOfFilms:List<Film>) {
        println("-------------->ON REPO PUT: ")
        filmDao.insertAll(pageOfFilms)
    }

    fun getPageOfFilmsInCategory(page: Int, category: String): Observable<List<Film>> {
        println("-------------->ON REPO Ge:")
        return filmDao.getCachedFilmsByPageAndCategory(page, category)
    }

    fun deleteAll() {
        filmDao.deleteAll()
    }

}