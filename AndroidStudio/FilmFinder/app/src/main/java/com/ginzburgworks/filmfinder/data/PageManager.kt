package com.ginzburgworks.filmfinder.data

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ginzburgworks.filmfinder.viewmodel.HomeFragmentViewModel


private const val PAGE_SIZE = 20
private const val ITEMS_AFTER_START = 7
private const val ITEMS_BEFORE_END = 5

open class PageManager(
    private val viewModel: HomeFragmentViewModel,
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {


    private var isPageRequested = false

    object Page {
        const val First = 1
        var Next = 1
        val Last: Int by lazy { HomeFragmentViewModel().lastPage }
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        if (isNeedToRequestNextPage(dy)) {
            viewModel.requestNextPage(++Page.Next)
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

    private fun isNotLastPage() = Page.Next < Page.Last

    private fun isScrollingDown(dy: Int) = dy > 0

    private fun isNeedToRequestNextPage(dy: Int) =
        isPageOnEnd() && !isPageRequested && isScrollingDown(dy) && isNotLastPage()

}
