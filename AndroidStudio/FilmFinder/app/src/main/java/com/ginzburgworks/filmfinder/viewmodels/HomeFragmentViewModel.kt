package com.ginzburgworks.filmfinder.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.shared.KEY_FILMS_CATEGORY
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.filmfinder.domain.PagesController
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.NEXT_PAGE
import com.ginzburgworks.filmfinder.domain.SingleLiveEvent
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 600000

class HomeFragmentViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var filmsAdapter: FilmListRecyclerAdapter

    private lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    val isLoading = ObservableBoolean()
    var isPageRequested = false
    val isProgressBarVisible = ObservableBoolean()
    val itemsForSearch = mutableListOf<Film>()
    val errorEvent = SingleLiveEvent<String>()
    private val exceptionHandler =
        CoroutineExceptionHandler { _, e -> errorEvent.postValue(App.instance.getString(R.string.exc_handler_msg) + e) }
    private val homeFragmentViewModelContext =
        viewModelScope.coroutineContext.plus(exceptionHandler + Dispatchers.IO)
    private val homeFragmentViewModelScope = CoroutineScope(homeFragmentViewModelContext)

    init {
        App.instance.appComponent.injectHomeVM(this)
        subscribeForCategoryChanges()
    }

    fun getTotalNumberOfPages() = interactor.getTotalPagesNumber()

    fun requestNextPage() {
        isPageRequested = true
        showProgressBar()
        requestNextPageFromDataSource()
    }

    private fun requestNextPageFromDataSource() {
        if (isLocalDataSourceNeedToUpdate()) requestNextPageFromRemote()
        else requestNextPageFromLocal()
    }

    private fun clearLocalDataSource() {
        homeFragmentViewModelScope.launch {
            interactor.clearLocalDataSource()
        }
    }

    private fun isLocalDataSourceNeedToUpdate(): Boolean {
        if (isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLocalDataSourceUpdateTime())) {
            clearLocalDataSource()
            return true
        }
        return false
    }

    private fun requestNextPageFromRemote() {
        homeFragmentViewModelScope.launch {
            interactor.requestPageOfFilmsFromRemoteDataSource(NEXT_PAGE)
            getNextPageFromDataSource()
        }
    }

    private fun requestNextPageFromLocal() {
        homeFragmentViewModelScope.launch {
            interactor.requestPageOfFilmsFromLocalDataSource(NEXT_PAGE)
            getNextPageFromDataSource()
        }
    }

    private fun getNextPageFromDataSource() {
        homeFragmentViewModelScope.launch {
            withContext(Dispatchers.Main) {
                interactor.pageFromDataSourceToUI.let {
                    for (element in it) {
                        filmsAdapter.addItems(element)
                    }
                }
            }
        }
    }

    private fun isLastUpdateEarlierThanPredefinedMaxTime(updateTimeInMs: Long): Boolean {
        val currentTimeInMs = Calendar.getInstance().timeInMillis
        return (currentTimeInMs - updateTimeInMs) > MAX_TIME_AFTER_BD_UPDATE
    }

    private fun subscribeForCategoryChanges() {
        onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == KEY_FILMS_CATEGORY) {
                    refreshData()
                }
            }.also { registerOnChangeListener(it) }
    }

    fun refreshData() {
        isLoading.set(true)
        filmsAdapter.clearItems()
        clearPageCount()
        requestNextPage()
        isLoading.set(false)
    }

    private fun clearPageCount() {
        NEXT_PAGE = PagesController.FIRST_PAGE
    }

    fun reloadOnTextChange(result: List<Film>) {
        filmsAdapter.clearItems()
        filmsAdapter.addItems(result)
    }

    fun reloadAfterSearch(): Boolean {
        filmsAdapter.clearItems()
        filmsAdapter.addItems(itemsForSearch)
        return true
    }

    private fun showProgressBar() {
        interactor.progressBarScope.launch {
            for (element in interactor.progressBarState) isProgressBarVisible.set(element)
        }
    }

    private fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        interactor.registerPreferencesListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        interactor.progressBarScope.cancel()
        if (this::onSharedPreferenceChangeListener.isInitialized) interactor.unRegisterPreferencesListener(
            onSharedPreferenceChangeListener
        )
    }
}
