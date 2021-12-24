package com.ginzburgworks.filmfinder.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ginzburgworks.filmfinder.data.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms(): List<Film>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)
}