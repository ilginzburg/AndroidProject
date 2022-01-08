package com.ginzburgworks.filmfinder.data.db

import androidx.lifecycle.LiveData
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.db.dao.FilmDao
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {


    fun putPageOfFilmsToDb(pageOfFilms: List<Film>) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(pageOfFilms)
        }
    }

    fun getPageOfFilmsInCategoryFromDB(page: Int, category: String): LiveData<List<Film>> {
        return filmDao.getCachedFilmsByPageAndCategory(page, category)
    }

    fun deleteDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.delete()
        }
    }

}