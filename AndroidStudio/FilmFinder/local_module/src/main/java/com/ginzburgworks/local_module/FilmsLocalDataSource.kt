package com.ginzburgworks.local_module

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ginzburgworks.local_module.dao.FilmDao

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class FilmsLocalDataSource : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}