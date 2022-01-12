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
private const val TABLE_NAME = "cached_films"
private const val ID_DEFAULT_VALUE = 1
private const val PAGE_DEFAULT_VALUE = 1
private const val CATEGORY_DEFAULT_VALUE = "popular"
private const val TITLE_DEFAULT_VALUE = "defaultTitle"
private const val POSTER_DEFAULT_VALUE = "R.drawable.tv_default"
private const val DESCRIPTION_DEFAULT_VALUE = "descriptionDefault"
private const val RATING_DEFAULT_VALUE = 2.2
private const val IS_IN_FAVORITES_INITIAL_VALUE = false

@Parcelize
@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = [PAGE_NAME, CATEGORY_NAME, TITLE_NAME], unique = true)]
)
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = PAGE_NAME) val page: Int,
    @ColumnInfo(name = CATEGORY_NAME) val category: String,
    @ColumnInfo(name = TITLE_NAME) val title: String,
    @ColumnInfo(name = POSTER_PATH_NAME) val poster: String,
    @ColumnInfo(name = DESCRIPTION_NAME) val description: String,
    @ColumnInfo(name = RATING_NAME) var rating: Double,
    var isInFavorites: Boolean = false
) : Parcelable


object DefaultFilm {
    private const val default_id = ID_DEFAULT_VALUE
    private const val default_page = PAGE_DEFAULT_VALUE
    private const val default_category = CATEGORY_DEFAULT_VALUE
    private const val default_title = TITLE_DEFAULT_VALUE
    private const val default_poster = POSTER_DEFAULT_VALUE
    private const val default_description = DESCRIPTION_DEFAULT_VALUE
    private const val default_rating = RATING_DEFAULT_VALUE
    val film = Film(
        default_id,
        default_page,
        default_category,
        default_title,
        default_poster,
        default_description,
        default_rating
    )
}