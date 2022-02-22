package com.ginzburgworks.filmfinder.domain

import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R

private const val POPULAR_PAGES_DEFAULT = 500
private const val TOP_RATED_PAGES_DEFAULT = 474
private const val UPCOMING_PAGES_DEFAULT = 41
private const val NOW_PLAYING_PAGES_DEFAULT = 83

class PagesController {

    companion object {

        const val MAX_PAGES_NUM = 500
        const val MIN_PAGES_NUM = 10
        const val FIRST_PAGE = 1
        var NEXT_PAGE = FIRST_PAGE

        fun getDefaultTotalPagesByCategory(category: String): Int {
            return when (category) {
                App.instance.getString(R.string.top_rated_category) -> TOP_RATED_PAGES_DEFAULT
                App.instance.getString(R.string.upcoming_category) -> UPCOMING_PAGES_DEFAULT
                App.instance.getString(R.string.now_playing_category) -> NOW_PLAYING_PAGES_DEFAULT
                App.instance.getString(R.string.popular_category) -> POPULAR_PAGES_DEFAULT
                else -> UPCOMING_PAGES_DEFAULT
            }
        }

        const val PAGE_SIZE = 20
        const val ITEMS_BEFORE_END = 5
        const val ITEMS_AFTER_START = 7
    }
}
