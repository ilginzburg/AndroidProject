package com.ginzburgworks.filmfinder.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.db.dao.FilmDao
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {


    fun putPageOfFilmsToDb(pageOfFilms: List<Film>) {
        Executors.newSingleThreadExecutor().execute {
            Log.i("--------->REPO_PUT","${pageOfFilms[0].category}, ${pageOfFilms[0].page} ")
            filmDao.insertAll(pageOfFilms)
        }
    }

    fun getPageOfFilmsInCategoryFromDB(page: Int, category: String): LiveData<List<Film>> {
        Log.i("--------->REPO_GET","$category, $page ")
        return filmDao.getCachedFilmsByPageAndCategory(page, category)
    }

    fun deleteDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.delete()
        }
    }

}