package com.ginzburgworks.filmfinder.domain

import android.content.SharedPreferences
import com.ginzburgworks.filmfinder.data.local.DefaultFilm
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.data.remote.API
import com.ginzburgworks.filmfinder.data.remote.TmdbApi
import com.ginzburgworks.filmfinder.data.remote.TmdbApiSearch
import com.ginzburgworks.filmfinder.data.remote.entity.TmdbResultsDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

private const val SEARCH_CATEGORY_NAME = "search"

class Interactor(private val repo: FilmsRepository,
    private val retrofitService: TmdbApi,
    private val retrofitServiceSearch: TmdbApiSearch,
    private val preferenceProvider: PreferenceProvider) {

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private lateinit var tmdbResultsDto: Single<TmdbResultsDto>
    private val searchResults = mutableListOf<Film>()
    val disposables = mutableListOf<Disposable?>()
    val searchResultsToUi = sendSearchResultsToUi()

    fun requestPageOfFilmsFromRemoteDataSource(page: Int, isOnSearch: Boolean, searchQuery: String) {
        var category = preferenceProvider.getFilmsCategory()
        progressBarState.onNext(true)
        if (isOnSearch) {
            category = SEARCH_CATEGORY_NAME
            tmdbResultsDto = retrofitServiceSearch.getSearchResult(API.KEY, "ru-RU", page, searchQuery, false)
        } else tmdbResultsDto = retrofitService.getFilms(category, API.KEY, "ru-RU", page)
        subscribeToPageOfFilms(category, isOnSearch)
    }


    private fun subscribeToPageOfFilms(category: String, isOnSearch: Boolean) {
        tmdbResultsDto.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnError {
            it.printStackTrace()
            progressBarState.onNext(false)
        }.subscribe({ dto ->
            (dto.tmdbFilms).map {
                Film(it.id ?: DefaultFilm.film.id,
                    dto.page,
                    category,
                    it.title ?: DefaultFilm.film.title,
                    it.posterPath ?: DefaultFilm.film.poster,
                    it.overview ?: DefaultFilm.film.description,
                    it.voteAverage ?: DefaultFilm.film.rating)
            }.also {
                if (!isOnSearch) {
                    putPageOfFilmsToLocal(it)
                    saveTotalPagesNumber(dto.totalPages)
                    saveLocalDataSourceUpdateTime()
                } else {
                    searchResults.apply {
                        clear()
                        addAll(it)
                        searchResultsToUi.onNext(this)
                    }
                }
            }
            progressBarState.onNext(false)
        }, {
            it.printStackTrace()
        }).let { disposables.add(it) }
    }


    private fun putPageOfFilmsToLocal(list: List<Film>) = Completable.fromAction {
        repo.putPageOfFilms(list)
    }.subscribeOn(Schedulers.io()).subscribe({ }, Throwable::printStackTrace).let { disposables.add(it) }

    private fun sendSearchResultsToUi(): PublishSubject<List<Film>> = PublishSubject.create()

    fun requestPageOfFilmsFromLocalDataSource(page: Int): Observable<List<Film>> =
        repo.getPageOfFilmsInCategory(page, preferenceProvider.getFilmsCategory())

    fun clearLocalDataSource() = repo.deleteAll()

    fun getCurrentFilmsCategory() = preferenceProvider.getFilmsCategory()

    fun saveCurrentFilmsCategory(category: String) = preferenceProvider.saveFilmsCategory(category)

    fun getTotalPagesNumber() = preferenceProvider.getTotalPagesNumber(getCurrentFilmsCategory())

    private fun saveTotalPagesNumber(totalPagesNumber: Int) =
        preferenceProvider.saveTotalPagesNumber(checkTotalPagesNumber(totalPagesNumber), getCurrentFilmsCategory())

    private fun checkTotalPagesNumber(num: Int?): Int {
        val default = PagesController.getDefaultTotalPagesByCategory(getCurrentFilmsCategory())
        return when (num) {
            !in PagesController.MIN_PAGES_NUM..PagesController.MAX_PAGES_NUM -> default
            else -> num ?: default
        }
    }

    fun getNightMode(): Int = preferenceProvider.getNightModeSetting()

    fun saveNightMode(mode: Int) = preferenceProvider.saveNightModeSetting(mode)

    fun getLocalDataSourceUpdateTime() = preferenceProvider.getLocalDataSourceUpdateTime()

    private fun saveLocalDataSourceUpdateTime() {
        val dbUpdateTime = Calendar.getInstance().timeInMillis
        preferenceProvider.saveLocalDataSourceUpdateTime(dbUpdateTime)
    }

    fun registerPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) = preferenceProvider.registerListener(listener)

    fun unRegisterPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) = preferenceProvider.unRegisterListener(listener)

}

