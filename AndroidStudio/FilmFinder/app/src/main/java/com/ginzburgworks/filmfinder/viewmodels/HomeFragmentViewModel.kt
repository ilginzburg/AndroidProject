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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observables.ConnectableObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 60000 //!!!!

class HomeFragmentViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var filmsAdapter: FilmListRecyclerAdapter

    private lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    val isLoading = ObservableBoolean()
    var isPageRequested = false
    val showProgressBar: BehaviorSubject<Boolean>
    val isProgressBarVisible = ObservableBoolean()
    val errorEvent = SingleLiveEvent<String>()
    val searchResults: PublishSubject<List<Film>> by lazy { requestSearchResults() }
    val itemsSavedBeforeSearch = mutableListOf<Film>()
    var searchQuery = ""
    val disposables = mutableListOf<Disposable?>()
    private val filmsListData: Observable<List<Film>> by lazy { requestPageOfFilms() }


    val connectableObservable: ConnectableObservable<List<Film>> by lazy { getConnectableObs() }


    init {
        App.instance.appComponent.injectHomeVM(this)
        showProgressBar = interactor.progressBarState
        disposables.addAll(interactor.disposables)
        subscribeForCategoryChanges()
    }


    fun requestNextPage() {
        isPageRequested = true
        checkIfLocalDataSourceNeedToUpdate()
        requestPageOfFilms()
    }

    private fun requestPageOfFilms(): Observable<List<Film>> {
        return interactor.requestPageOfFilmsFromDataSource()
    }

    private fun getConnectableObs(): ConnectableObservable<List<Film>> {
        return filmsListData.observeOn(AndroidSchedulers.mainThread()).replay(1)
    }

    private fun clearLocalDataSource() {
        Completable.fromAction {
            interactor.clearLocalDataSource()
        }.subscribeOn(Schedulers.io()).subscribe({}, {
            it.printStackTrace()
        }).let { disposables.add(it) }
    }

    private fun checkIfLocalDataSourceNeedToUpdate() {
        if (isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLocalDataSourceUpdateTime())) {
            clearLocalDataSource()
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
        clearLocalDataSource()
        clearPageCount()
        requestNextPage()
        isLoading.set(false)
    }

    private fun clearPageCount() {
        NEXT_PAGE = PagesController.FIRST_PAGE
    }

    fun requestSearchResults(): PublishSubject<List<Film>> {
        requestNextPageOfSearchResults(searchQuery)
        return interactor.searchResultsToUi
    }

    private fun requestNextPageOfSearchResults(searchQuery: String) {
        interactor.getSearchResults(searchQuery)
    }

    fun reloadOnSearch(list: List<Film>): Boolean = filmsAdapter.run {
        clearItems()
        addItems(list)
        true
    }

    fun getTotalNumberOfPages() = interactor.getTotalPagesNumber()

    private fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        interactor.registerPreferencesListener(listener)

    override fun onCleared() {
        super.onCleared()
        if (this::onSharedPreferenceChangeListener.isInitialized) interactor.unRegisterPreferencesListener(
            onSharedPreferenceChangeListener
        )
    }
}
