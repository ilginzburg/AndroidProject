package com.ginzburgworks.filmfinder.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.db.dao.FilmDao

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}