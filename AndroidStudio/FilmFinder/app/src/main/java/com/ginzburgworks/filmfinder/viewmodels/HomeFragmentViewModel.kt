package com.ginzburgworks.filmfinder.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.domain.SingleLiveEvent
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.FIRST_PAGE
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.NEXT_PAGE
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 600000

class HomeFragmentViewModel @Inject constructor(val interactor: Interactor): ViewModel() {

    @Inject
    lateinit var adapter: FilmListRecyclerAdapter
    val itemsForSearch = mutableListOf<Film>()
    lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    var totalNumberOfPages = interactor.getTotalPagesNumber()
    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    private val currentPageLiveData = MutableLiveData<Int>()
    val filmsListLiveData = Transformations.switchMap(currentPageLiveData) { page ->
        interactor.getPageOfFilmsFromLocalDataSource(page)
    }

    val errorEvent = SingleLiveEvent<String>()

    var lastReadPageNumAndCategory: Pair<Int, String> = 0 to ""

    private val handler =
        CoroutineExceptionHandler { _, e -> errorEvent.postValue(App.instance.getString(R.string.exc_handler_msg) + e) }

    init {
        requestNextPage(FIRST_PAGE)
    }

    fun requestNextPage(page: Int) {
        val isPageInDataBaseOutdated =
            isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLocalDataSourceUpdateTime())
        if (isPageInDataBaseOutdated) {
            viewModelScope.launch(handler) {
                launch {
                    interactor.clearLocalDataSource()
                }
            }
        }
        requestNextPageFromLocal(page)
    }

    fun requestNextPageFromRemote() {
        showProgressBar.postValue(true)
        viewModelScope.launch(handler) {
            launch {
                interactor.getPageOfFilmsFromRemoteDataSource(NEXT_PAGE)
                showProgressBar.postValue(false)
            }
        }
    }

    private fun requestNextPageFromLocal(page: Int) {
        currentPageLiveData.value = page
    }

    private fun isLastUpdateEarlierThanPredefinedMaxTime(timeBDUpdatedInMs: Long): Boolean {
        val currentTimeInMs = Calendar.getInstance().timeInMillis
        return (currentTimeInMs - timeBDUpdatedInMs) > MAX_TIME_AFTER_BD_UPDATE
    }

    fun registerOnChangeListener() {
        interactor.registerPreferencesListener(onSharedPreferenceChangeListener)
    }

    override fun onCleared() {
        super.onCleared()
        if (this::onSharedPreferenceChangeListener.isInitialized)
            interactor.unRegisterPreferencesListener(onSharedPreferenceChangeListener)
    }

}