package com.ginzburgworks.filmfinder.domain

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.view.fragments.SettingsFragment
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel


private const val PAGE_SIZE = 20
private const val ITEMS_AFTER_START = 7
private const val ITEMS_BEFORE_END = 5
private const val POPULAR_PAGES_DEFAULT = 500
private const val TOP_RATED_PAGES_DEFAULT = 474
private const val UPCOMING_PAGES_DEFAULT = 41
private const val NOW_PLAYING_PAGES_DEFAULT = 83

class PagesController(
    private val viewModel: HomeFragmentViewModel,
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    private var isPageRequested = false
    private lateinit var totalNumberOfPagesInCurrentCategory:Number

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        totalNumberOfPagesInCurrentCategory = viewModel.totalNumberOfPages

        if (isNeedToRequestNextPage(dy)) {
            viewModel.requestNextPage(++NEXT_PAGE)
            isPageRequested = true
        }
        if (isPageOnStart())
            isPageRequested = false

    }

    private fun bottomItemPosition() = layoutManager.findLastCompletelyVisibleItemPosition()

    private fun startPagePosition() = layoutManager.itemCount - PAGE_SIZE + ITEMS_AFTER_START

    private fun endPagePosition() = layoutManager.itemCount - ITEMS_BEFORE_END

    private fun isPageOnStart() = bottomItemPosition() == startPagePosition()

    private fun isPageOnEnd() = bottomItemPosition() == endPagePosition()

    private fun isNotLastPage() = NEXT_PAGE < totalNumberOfPagesInCurrentCategory.toInt()

    private fun isScrollingDown(dy: Int) = dy > 0

    private fun isNeedToRequestNextPage(dy: Int) =
        isPageOnEnd() && !isPageRequested && isScrollingDown(dy) && isNotLastPage()

    fun restartPages() {
        isPageRequested = false
        NEXT_PAGE = FIRST_PAGE
        viewModel.requestNextPage(NEXT_PAGE)
        isPageRequested = true
    }

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
    }
}
