package com.ginzburgworks.filmfinder.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ginzburgworks.filmfinder.data.Film

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    @Query("SELECT * FROM cached_films WHERE page=:requestedPage AND category=:requestedCategory")
    fun getCachedFilmsByPageAndCategory(requestedPage: Int, requestedCategory:String): LiveData<List<Film>>

    @Query("DELETE FROM cached_films")
    fun delete()
}