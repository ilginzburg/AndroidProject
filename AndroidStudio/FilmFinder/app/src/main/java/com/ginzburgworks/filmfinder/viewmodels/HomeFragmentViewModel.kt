package com.ginzburgworks.filmfinder.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.shared.KEY_FILMS_CATEGORY
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.filmfinder.domain.PagesController
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.NEXT_PAGE
import com.ginzburgworks.filmfinder.domain.SingleLiveEvent
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 600000

class HomeFragmentViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var filmsAdapter: FilmListRecyclerAdapter

    lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    val isLoading = ObservableBoolean()
    var isPageRequested = false
    val isProgressBarVisible = ObservableBoolean()
    val itemsForSearch = mutableListOf<Film>()
    val errorEvent = SingleLiveEvent<String>()
    val filmsListData: Observable<List<Film>>
    val showProgressBar: BehaviorSubject<Boolean>

    init {
        App.instance.appComponent.injectHomeVM(this)
        showProgressBar = interactor.progressBarState
        filmsListData = interactor.requestPageOfFilmsFromLocalDataSource(NEXT_PAGE)
        subscribeForCategoryChanges()
    }

    fun requestNextPage() {
        isPageRequested = true
        requestNextPageFromDataSource()
    }

    private fun requestNextPageFromDataSource() {
        if (isLocalDataSourceNeedToUpdate())
            requestNextPageFromRemote()
        else
            requestNextPageFromLocal()
    }

    private fun requestNextPageFromRemote() {
        interactor.requestPageOfFilmsFromRemoteDataSource(NEXT_PAGE)
    }

    private fun requestNextPageFromLocal() {
        interactor.requestPageOfFilmsFromLocalDataSource(NEXT_PAGE)
    }

    private fun clearLocalDataSource() {
        Completable.fromAction {
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

    fun getTotalNumberOfPages() = interactor.getTotalPagesNumber()

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
        PagesController.NEXT_PAGE = PagesController.FIRST_PAGE
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

    private fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        interactor.registerPreferencesListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        if (this::onSharedPreferenceChangeListener.isInitialized)
            interactor.unRegisterPreferencesListener(onSharedPreferenceChangeListener)
    }
}
