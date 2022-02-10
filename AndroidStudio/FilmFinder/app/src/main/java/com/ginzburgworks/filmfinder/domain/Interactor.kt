package com.ginzburgworks.filmfinder.domain

import android.content.SharedPreferences
import com.ginzburgworks.filmfinder.data.local.DefaultFilm
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.data.remote.API
import com.ginzburgworks.filmfinder.data.remote.TmdbApi
import com.ginzburgworks.filmfinder.data.remote.TmdbApiSearch
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.NEXT_PAGE
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

private const val SEARCH_CATEGORY_NAME = "search"

class Interactor(
    private val repo: FilmsRepository,
    private val retrofitService: TmdbApi,
    private val retrofitServiceSearch: TmdbApiSearch,
    private val preferenceProvider: PreferenceProvider
) {

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val disposables = mutableListOf<Disposable?>()
    val searchResultsToUi: PublishSubject<List<Film>> by lazy { createSearchResultsToUi() }
    val filmsListData: Observable<List<Film>> by lazy { requestPageOfFilmsFromDataSource() }


    fun requestPageOfFilmsFromDataSource(): Observable<List<Film>> {
        progressBarState.onNext(true)
        val category = preferenceProvider.getFilmsCategory()
        return repo.getPageOfFilmsInCategory(NEXT_PAGE, category).filter { it.isNotEmpty() }
            .switchIfEmpty(getFromRemote(category).also { it ->
                it.observeOn(Schedulers.io())
                it.subscribeOn(Schedulers.io())
                it.subscribe({
                    repo.putPageOfFilms(it)
                    progressBarState.onNext(false)
                }, {
                    it.printStackTrace()
                })
            })
    }


    private fun getFromRemote(category: String): Observable<List<Film>> {
        return retrofitService.getFilms(category, API.KEY, "ru-RU", NEXT_PAGE)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .map { (page, tmdbFilms, totalPages) ->
                (tmdbFilms).map {
                    Film(
                        it.id ?: DefaultFilm.film.id,
                        page,
                        category,
                        it.title ?: DefaultFilm.film.title,
                        it.posterPath ?: DefaultFilm.film.poster,
                        it.overview ?: DefaultFilm.film.description,
                        it.voteAverage ?: DefaultFilm.film.rating
                    )
                }.also {
                    saveTotalPagesNumber(totalPages)
                    saveLocalDataSourceUpdateTime()
                }
            }.toObservable()
    }


    fun getSearchResults(searchQuery: String) {
        retrofitServiceSearch.getSearchResult(API.KEY, "ru-RU", NEXT_PAGE, searchQuery, false)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .map { (page, tmdbFilms, totalPages) ->
                (tmdbFilms).map {
                    Film(
                        it.id ?: DefaultFilm.film.id,
                        page,
                        SEARCH_CATEGORY_NAME,
                        it.title ?: DefaultFilm.film.title,
                        it.posterPath ?: DefaultFilm.film.poster,
                        it.overview ?: DefaultFilm.film.description,
                        it.voteAverage ?: DefaultFilm.film.rating
                    )
                }.also {
                    saveTotalPagesNumber(totalPages)
                }
            }.apply { searchResultsToUi }
    }


    private fun createSearchResultsToUi(): PublishSubject<List<Film>> = PublishSubject.create()

    fun clearLocalDataSource() = repo.deleteAll()

    fun getCurrentFilmsCategory() = preferenceProvider.getFilmsCategory()

    fun saveCurrentFilmsCategory(category: String) = preferenceProvider.saveFilmsCategory(category)

    fun getTotalPagesNumber() = preferenceProvider.getTotalPagesNumber(getCurrentFilmsCategory())

    private fun saveTotalPagesNumber(totalPagesNumber: Int) =
        preferenceProvider.saveTotalPagesNumber(
            checkTotalPagesNumber(totalPagesNumber), getCurrentFilmsCategory()
        )

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

    fun registerPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        preferenceProvider.registerListener(listener)

    fun unRegisterPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        preferenceProvider.unRegisterListener(listener)

}

