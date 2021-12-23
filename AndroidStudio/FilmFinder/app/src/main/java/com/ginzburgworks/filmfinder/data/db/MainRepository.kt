package com.ginzburgworks.filmfinder.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ginzburgworks.filmfinder.domain.Film
import com.ginzburgworks.filmfinder.view.rv_viewholders.FilmViewHolder.Companion.defaultFilm
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
            cursor.close()
        }
        return result
    }

    fun getFilmByTitle(title: String): Film {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_TITLE} LIKE \"$title\" ",
            null
        )
        var result = defaultFilm
        if (cursor.moveToFirst()) {
            do {
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)
                result = Film(title, poster, description, rating)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return result
    }

    fun updateFilmByRowId(rowId: Int, film: Film) {
        val sqliteString = "UPDATE ${DatabaseHelper.TABLE_NAME} SET ${DatabaseHelper.COLUMN_TITLE} = \"${film.title}\",${DatabaseHelper.COLUMN_POSTER} = \"${film.poster}\",${DatabaseHelper.COLUMN_DESCRIPTION} = \"${film.description}\", ${DatabaseHelper.COLUMN_RATING} = \"${film.rating}\" WHERE ID = $rowId "
        sqlDb.execSQL(sqliteString)
    }
}
