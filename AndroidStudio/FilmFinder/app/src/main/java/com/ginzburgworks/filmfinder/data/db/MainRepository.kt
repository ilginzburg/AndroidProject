package com.ginzburgworks.filmfinder.data.db

import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.db.dao.FilmDao
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {

    fun putToDb(films: List<Film>) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getAllFromDB(): List<Film> {
        return filmDao.getCachedFilms()
    }
}