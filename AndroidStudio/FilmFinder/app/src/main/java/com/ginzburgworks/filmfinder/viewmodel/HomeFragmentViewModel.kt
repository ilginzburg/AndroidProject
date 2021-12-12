package com.ginzburgworks.filmfinder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.data.PageManager
import com.ginzburgworks.filmfinder.domain.Film
import com.ginzburgworks.filmfinder.domain.Interactor
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton


class HomeFragmentViewModel  @Inject constructor(private val interactor: Interactor)  : ViewModel() {

    val filmsListLiveData = MutableLiveData<List<Film>>()
    val itemsForSearch = mutableListOf<Film>()

    init {
        requestNextPage(PageManager.Page.First)
    }

    fun requestNextPage(page: Int) {
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>, totalPages: Int) {
                PageManager.Page.Last = totalPages
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
            }
        })
    }

    interface ApiCallback {
        fun onSuccess(films: List<Film>, totalPages: Int)
        fun onFailure()
    }
}