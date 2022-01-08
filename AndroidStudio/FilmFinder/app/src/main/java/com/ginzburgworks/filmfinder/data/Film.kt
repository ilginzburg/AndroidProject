package com.ginzburgworks.filmfinder.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cached_films", indices = [Index(value = ["page","category","title"], unique = true)])
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "page")val page: Int,
    @ColumnInfo(name = "category")val category: String,
    @ColumnInfo(name = "title")val title: String,
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "overview") val description: String,
    @ColumnInfo(name = "vote_average") var rating: Double = 0.0,
    var isInFavorites: Boolean = false
) : Parcelable