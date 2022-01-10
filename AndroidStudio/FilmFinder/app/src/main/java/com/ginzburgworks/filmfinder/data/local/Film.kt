package com.ginzburgworks.filmfinder.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

private const val PAGE = "page"
private const val CATEGORY = "category"
private const val TITLE = "title"
private const val POSTER_PATH = "poster_path"
private const val DESCRIPTION = "overview"
private const val RATING = "vote_average"

@Parcelize
@Entity(
    tableName = "cached_films",
    indices = [Index(value = [PAGE, CATEGORY, TITLE], unique = true)]
)
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = PAGE) val page: Int,
    @ColumnInfo(name = CATEGORY) val category: String,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = POSTER_PATH) val poster: String,
    @ColumnInfo(name = DESCRIPTION) val description: String,
    @ColumnInfo(name = RATING) var rating: Double = 0.0,
    var isInFavorites: Boolean = false
) : Parcelable