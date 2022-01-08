package com.ginzburgworks.filmfinder.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.PageManager.Companion.FIRST_PAGE
import com.ginzburgworks.filmfinder.data.PageManager.Companion.NEXT_PAGE
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import java.util.*
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 600000

class HomeFragmentViewModel @Inject constructor(
    val interactor: Interactor,
    application: Application
) : AndroidViewModel(application) {

    @Inject
    lateinit var adapter: FilmListRecyclerAdapter
    val itemsForSearch = mutableListOf<Film>()
    lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    var totalNumberOfPages =
        interactor.getTotalPagesNumberFromPreferences(interactor.getFilmsCategoryFromPreferences())
    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    private val currentPageLiveData = MutableLiveData(FIRST_PAGE)
    val filmsListLiveData = Transformations.switchMap(currentPageLiveData) { page ->
        interactor.getPageOfFilmsFromDB(page)
    }


    init {
        requestNextPage(FIRST_PAGE)
    }

    fun requestNextPage(page: Int) {
        val isPageInDataBaseOutdated =
            isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLastUpdateTimeFromPreferences())
        if (isPageInDataBaseOutdated)
            interactor.deleteDB()
        requestNextPageFromDB(page)
    }

    fun requestNextPageFromNetwork() {
        showProgressBar.postValue(true)
        interactor.getFilmsFromApi(NEXT_PAGE, object : ApiCallback {
            override fun onSuccess() {
                showProgressBar.postValue(false)
            }

            override fun onFailure() {
                showProgressBar.postValue(false)
            }
        })
    }

    private fun requestNextPageFromDB(page: Int) {
        currentPageLiveData.value = page
    }


    private fun isLastUpdateEarlierThanPredefinedMaxTime(timeBDUpdatedInMs: Long): Boolean {
        val currentTimeInMs = Calendar.getInstance().timeInMillis
        return (currentTimeInMs - timeBDUpdatedInMs) > MAX_TIME_AFTER_BD_UPDATE
    }

    interface ApiCallback {
        fun onSuccess()
        fun onFailure()
    }

    override fun onCleared() {
        super.onCleared()
        if (this::onSharedPreferenceChangeListener.isInitialized)
            interactor.preferenceProvider.unRegisterListener(onSharedPreferenceChangeListener)
    }

}