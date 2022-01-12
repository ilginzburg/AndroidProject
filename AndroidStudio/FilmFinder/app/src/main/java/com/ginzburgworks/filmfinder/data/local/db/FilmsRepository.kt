package com.ginzburgworks.filmfinder.data.local.db

import androidx.lifecycle.LiveData
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.dao.FilmDao

class FilmsRepository(private val filmDao: FilmDao) {

    suspend fun putPageOfFilms(pageOfFilms: List<Film>) {
        filmDao.insertAll(pageOfFilms)
    }

    suspend fun getPageOfFilmsInCategory(page: Int, category: String): List<Film>{
        return filmDao.getCachedFilmsByPageAndCategory(page, category)
    }

    suspend fun deleteAll() {
        filmDao.deleteAll()
    }

}