package com.ginzburgworks.filmfinder.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ginzburgworks.filmfinder.data.local.Film

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Film>)

    @Query("SELECT * FROM cached_films WHERE page=:requestedPage AND category=:requestedCategory")
    suspend fun getCachedFilmsByPageAndCategory(
        requestedPage: Int,
        requestedCategory: String
    ): List<Film>

    @Query("DELETE FROM cached_films")
    suspend fun deleteAll()
}