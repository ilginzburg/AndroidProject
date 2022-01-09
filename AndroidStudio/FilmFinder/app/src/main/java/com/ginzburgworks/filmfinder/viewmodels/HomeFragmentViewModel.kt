package com.ginzburgworks.filmfinder.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.PageManager.Companion.FIRST_PAGE
import com.ginzburgworks.filmfinder.data.PageManager.Companion.NEXT_PAGE
import com.ginzburgworks.filmfinder.data.SingleLiveEvent
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 600000
private const val FIRST_TIME_PAGE_DUMMY_CATEGORY = "first_time"

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

    private val currentPageLiveData = MutableLiveData<Int>()
    val filmsListLiveData = Transformations.switchMap(currentPageLiveData) { page ->
        interactor.getPageOfFilmsFromDB(page)
    }

    val errorEvent = SingleLiveEvent<String>()

    var lastReadPageNumAndCategory: Pair<Int, String> = 0 to FIRST_TIME_PAGE_DUMMY_CATEGORY


    init {
        requestNextPage(FIRST_PAGE)
    }

    fun requestNextPage(page: Int) {
        val isPageInDataBaseOutdated =
            isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLastUpdateTimeFromPreferences())
        if (isPageInDataBaseOutdated) {
            viewModelScope.launch {
                interactor.deleteDB()
            }
        }
        requestNextPageFromDB(page)
    }

    fun requestNextPageFromNetwork() {
        showProgressBar.postValue(true)
        viewModelScope.launch {
            interactor.getFilmsFromApi(NEXT_PAGE)
            showProgressBar.postValue(false)
        }
    }


    private fun requestNextPageFromDB(page: Int) {
        currentPageLiveData.value = page
    }

    private fun isLastUpdateEarlierThanPredefinedMaxTime(timeBDUpdatedInMs: Long): Boolean {
        val currentTimeInMs = Calendar.getInstance().timeInMillis
        return (currentTimeInMs - timeBDUpdatedInMs) > MAX_TIME_AFTER_BD_UPDATE
    }


    override fun onCleared() {
        super.onCleared()
        if (this::onSharedPreferenceChangeListener.isInitialized)
            interactor.preferenceProvider.unRegisterListener(onSharedPreferenceChangeListener)
    }

}