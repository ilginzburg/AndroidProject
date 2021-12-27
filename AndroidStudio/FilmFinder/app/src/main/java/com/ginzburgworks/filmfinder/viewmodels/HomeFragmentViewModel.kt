package com.ginzburgworks.filmfinder.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.PageManager
import com.ginzburgworks.filmfinder.domain.Interactor
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 600000

class HomeFragmentViewModel @Inject constructor(val interactor: Interactor) : ViewModel() {

    val filmsListLiveData = MutableLiveData<List<Film>>()
    val itemsForSearch = mutableListOf<Film>()
    lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    var totalNumberOfPages =
        interactor.getTotalPagesNumberFromPreferences(interactor.getFilmsCategoryFromPreferences())

    init {
        requestNextPage(PageManager.FIRST_PAGE)
    }

    fun requestNextPage(page: Int) {
        if (isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLastUpdateTimeFromPreferences())) {
            requestNextPageFromNetwork(page)
            interactor.deleteDB()
        }
        else if (!requestNextPageFromDB(page))
            requestNextPageFromNetwork(page)
    }

    private fun requestNextPageFromNetwork(page: Int) {
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(pageOfFilms: List<Film>, totalPages: Int) {
                filmsListLiveData.postValue(pageOfFilms)
                saveTotalPagesNumber(totalPages)
                saveUpdateDbTime()
            }

            override fun onFailure() {
                requestNextPageFromDB(page)
            }
        })
    }

    private fun requestNextPageFromDB(page: Int): Boolean {
        var pageOfFilmsFromDB: List<Film> = mutableListOf()
        val thread = Executors.newSingleThreadExecutor()
        thread.execute {
            pageOfFilmsFromDB = interactor.getPageOfFilmsFromDB(page)
        }
        thread.shutdown()
        thread.awaitTermination(6, TimeUnit.SECONDS)
        return if (pageOfFilmsFromDB.isNotEmpty()) {
            filmsListLiveData.postValue(pageOfFilmsFromDB)
            true
        } else  false
    }

    private fun saveTotalPagesNumber(totalPagesNumber: Int) {
        var totalPagesFromNetwork = PageManager.MAX_PAGES_NUM
        if (totalPagesNumber < PageManager.MAX_PAGES_NUM)
            totalPagesFromNetwork = totalPagesNumber
        interactor.saveTotalPagesNumberToPreferences(
            totalPagesFromNetwork,
            interactor.getFilmsCategoryFromPreferences()
        )
    }

    private fun saveUpdateDbTime() {
        val dbUpdateTime = Calendar.getInstance().timeInMillis
        interactor.saveUpdateDbTimeToPreferences(dbUpdateTime)
    }

    private fun isLastUpdateEarlierThanPredefinedMaxTime(timeBDUpdatedInMs: Long): Boolean {
        val currentTimeInMs = Calendar.getInstance().timeInMillis
        return (currentTimeInMs - timeBDUpdatedInMs) > MAX_TIME_AFTER_BD_UPDATE
    }

    interface ApiCallback {
        fun onSuccess(pageOfFilms: List<Film>, totalPages: Int)
        fun onFailure()
    }

    override fun onCleared() {
        super.onCleared()
        if (this::onSharedPreferenceChangeListener.isInitialized)
            interactor.preferenceProvider.unRegisterListener(onSharedPreferenceChangeListener)
    }

}