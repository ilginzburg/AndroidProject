package com.ginzburgworks.filmfinder.data.db

import androidx.lifecycle.LiveData
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.db.dao.FilmDao

class MainRepository(private val filmDao: FilmDao) {

    suspend fun putPageOfFilmsToDb(pageOfFilms: List<Film>) {
        filmDao.insertAll(pageOfFilms)
    }

    fun getPageOfFilmsInCategoryFromDB(page: Int, category: String): LiveData<List<Film>> {
        return filmDao.getCachedFilmsByPageAndCategory(page, category)
    }

    suspend fun deleteDB() {
        filmDao.delete()
    }

}