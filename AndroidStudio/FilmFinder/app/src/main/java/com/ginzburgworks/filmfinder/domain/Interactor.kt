package com.ginzburgworks.filmfinder.domain

import android.content.SharedPreferences
import com.ginzburgworks.filmfinder.data.local.DefaultFilm
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.data.remote.API
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.NEXT_PAGE
import com.ginzburgworks.remote_module.TmdbApi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

class Interactor(
    private val repo: FilmsRepository,
    private val retrofitService: TmdbApi,
    private val preferenceProvider: PreferenceProvider
) {

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val disposables = mutableListOf<Disposable?>()

    fun getFilms(): Observable<List<Film>> =
        repo.getFilmsInCategory(preferenceProvider.getFilmsCategory())

    fun putNewPageOfFilmsToLocalDataSource() {
        val category = preferenceProvider.getFilmsCategory()
        progressBarState.onNext(true)
        getPageOfFilmsFromRemote(category).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .subscribe({
                repo.putPageOfFilms(it)
                progressBarState.onNext(false)
            }, {
                it.printStackTrace()
                progressBarState.onNext(false)
            })
    }


    private fun getPageOfFilmsFromRemote(category: String): Observable<List<Film>> {
        return convertSingleApiToObservableDtoList(
            retrofitService.getFilms(
                category, API.KEY, "ru-RU", NEXT_PAGE
            )
        )
    }

    fun getSearchResults(searchQuery: String): Observable<List<Film>> =
        convertSingleApiToObservableDtoList(
            retrofitService.getSearchResult(
                API.KEY, "ru-RU", NEXT_PAGE, searchQuery, false
            )
        )

    private fun convertSingleApiToObservableDtoList(apiList: Single<com.ginzburgworks.remote_module.entity.TmdbResultsDto>): Observable<List<Film>> {
        return apiList.subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .map { (page, tmdbFilms, totalPages) ->
                (tmdbFilms).map {
                    Film(
                        it.id ?: DefaultFilm.film.id,
                        page,
                        preferenceProvider.getFilmsCategory(),
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

