package com.ginzburgworks.local_module.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ginzburgworks.local_module.Film
import io.reactivex.rxjava3.core.Observable

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    @Query("SELECT * FROM cached_films WHERE category=:requestedCategory")
    fun getCachedFilmsByCategory(
        requestedCategory: String
    ): Observable<List<Film>>

    @Query("DELETE FROM cached_films")
    fun deleteAll()
}