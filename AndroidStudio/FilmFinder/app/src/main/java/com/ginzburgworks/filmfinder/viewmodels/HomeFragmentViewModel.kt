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
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
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
    val showProgressBar: BehaviorSubject<Boolean>
    val isProgressBarVisible = ObservableBoolean()
    val errorEvent = SingleLiveEvent<String>()
    val itemsSavedBeforeSearch = mutableListOf<Film>()
    val disposables = mutableListOf<Disposable?>()
    var firstTimeLaunch = true
    var lastFirstVisiblePosition = 0

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

    private fun requestPageOfFilms() = interactor.putNewPageOfFilmsToLocalDataSource()

    fun getUpdatedFilms(): Observable<List<Film>> {
        checkIfLocalDataSourceNeedToUpdate()
        return interactor.getFilms()
    }

    private fun clearLocalDataSource() = Completable.fromAction {
        interactor.clearLocalDataSource()
    }.subscribeOn(Schedulers.io()).subscribe({}, {
        it.printStackTrace()
    }).let { disposables.add(it) }

    private fun checkIfLocalDataSourceNeedToUpdate() =
        isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLocalDataSourceUpdateTime()).let { if (it) clearLocalDataSource() }

    private fun isLastUpdateEarlierThanPredefinedMaxTime(updateTimeInMs: Long): Boolean =
        (Calendar.getInstance().timeInMillis - updateTimeInMs) > MAX_TIME_AFTER_BD_UPDATE

    private fun subscribeForCategoryChanges() =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_FILMS_CATEGORY) refreshData()
        }.also {
            onSharedPreferenceChangeListener = it
            registerOnChangeListener(it)
        }

    fun refreshData() {
        isLoading.set(true)
        filmsAdapter.clearItems()
        clearLocalDataSource()
        clearPageCount()
        requestNextPage()
        firstTimeLaunch = true
        isLoading.set(false)
    }

    private fun clearPageCount() = PagesController.FIRST_PAGE.let { NEXT_PAGE = it }

    fun requestSearchResults(searchQuery: String): Observable<List<Film>> =
        interactor.getSearchResults(searchQuery)

    fun reloadAdapterItems(list: List<Film>) = filmsAdapter.run {
        clearItems()
        addItems(list)
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
