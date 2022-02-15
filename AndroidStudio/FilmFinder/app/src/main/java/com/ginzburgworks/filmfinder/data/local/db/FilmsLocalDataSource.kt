package com.ginzburgworks.filmfinder.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.dao.FilmDao

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class FilmsLocalDataSource : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}