package com.ginzburgworks.filmfinder.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ginzburgworks.filmfinder.domain.Film
import javax.inject.Inject

class MainRepository @Inject constructor(databaseHelper: DatabaseHelper) {
    private val sqlDb = databaseHelper.readableDatabase
    private lateinit var cursor: Cursor

    fun putToDb(film: Film) {
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, film.title)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
        }
        sqlDb.insertWithOnConflict(
            DatabaseHelper.TABLE_NAME,
            null,
            cv,
            SQLiteDatabase.CONFLICT_IGNORE
        )
    }

    fun getAllFromDB(): List<Film> {
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
        val result = mutableListOf<Film>()
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)
                result.add(Film(title, poster, description, rating))
            } while (cursor.moveToNext())
        }
        return result
    }

    fun getFilmByTitle(title: String): Film {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_TITLE} LIKE \"$title\" ",
            null
        )
        cursor.moveToFirst()
        val poster = cursor.getString(2)
        val description = cursor.getString(3)
        val rating = cursor.getDouble(4)
        cursor.close()
        return Film(title, poster, description, rating)

    }


}