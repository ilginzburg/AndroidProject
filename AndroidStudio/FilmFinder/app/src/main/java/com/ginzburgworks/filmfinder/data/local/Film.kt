package com.ginzburgworks.filmfinder.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

private const val PAGE_NAME = "page"
private const val CATEGORY_NAME = "category"
private const val TITLE_NAME = "title"
private const val POSTER_PATH_NAME = "poster_path"
private const val DESCRIPTION_NAME = "overview"
private const val RATING_NAME = "vote_average"

@Parcelize
@Entity(
    tableName = "cached_films",
    indices = [Index(value = [PAGE_NAME, CATEGORY_NAME, TITLE_NAME], unique = true)]
)
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = PAGE_NAME) val page: Int,
    @ColumnInfo(name = CATEGORY_NAME) val category: String,
    @ColumnInfo(name = TITLE_NAME) val title: String,
    @ColumnInfo(name = POSTER_PATH_NAME) val poster: String,
    @ColumnInfo(name = DESCRIPTION_NAME) val description: String,
    @ColumnInfo(name = RATING_NAME) var rating: Double = 0.0,
    var isInFavorites: Boolean = false
) : Parcelable