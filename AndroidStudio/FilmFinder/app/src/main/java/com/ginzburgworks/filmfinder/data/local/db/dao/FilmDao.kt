package com.ginzburgworks.filmfinder.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ginzburgworks.filmfinder.data.local.Film
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    @Query("SELECT * FROM cached_films WHERE page=:requestedPage AND category=:requestedCategory")
    fun getCachedFilmsByPageAndCategory(
        requestedPage: Int,
        requestedCategory: String
    ): Observable<List<Film>>

    @Query("DELETE FROM cached_films")
    fun deleteAll()
}