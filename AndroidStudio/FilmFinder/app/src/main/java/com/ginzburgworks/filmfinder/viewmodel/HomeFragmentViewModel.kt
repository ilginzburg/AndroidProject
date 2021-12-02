package com.ginzburgworks.filmfinder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.data.PageManager
import com.ginzburgworks.filmfinder.domain.Film
import com.ginzburgworks.filmfinder.domain.Interactor

class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    private var interactor: Interactor = App.instance.interactor
    var lastPage = 10

    init {
        requestNextPage(PageManager.Page.First)
    }

    fun requestNextPage(page: Int) {
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>, totalPages: Int) {
                filmsListLiveData.postValue(films)
                this@HomeFragmentViewModel.lastPage = totalPages
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