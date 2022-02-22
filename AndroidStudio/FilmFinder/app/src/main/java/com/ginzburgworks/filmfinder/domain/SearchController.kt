package com.ginzburgworks.filmfinder.domain

import androidx.appcompat.widget.SearchView
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import java.util.*

class SearchController(
    private val filmsAdapter: FilmListRecyclerAdapter,
    private val viewModel: HomeFragmentViewModel
) :
    SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isEmpty()) {
            return true
        }
        val result = viewModel.itemsForSearch.filter {
            it.title.lowercase(Locale.getDefault())
                .contains(newText.lowercase(Locale.getDefault()))
        }
        filmsAdapter.clearItems()
        filmsAdapter.addItems(result)
        return false
    }
}