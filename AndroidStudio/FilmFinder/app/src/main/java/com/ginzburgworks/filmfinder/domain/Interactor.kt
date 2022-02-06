package com.ginzburgworks.filmfinder.domain

import android.content.SharedPreferences
import com.ginzburgworks.filmfinder.AutoDisposable
import com.ginzburgworks.filmfinder.addTo
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.data.remote.API
import com.ginzburgworks.filmfinder.data.remote.TmdbApi
import com.ginzburgworks.filmfinder.data.remote.entity.TmdbResultsDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

private const val SEARCH_CATEGORY_NAME = "search"

class Interactor(
    private val repo: FilmsRepository,
    private val retrofitService: TmdbApi,
    private val preferenceProvider: PreferenceProvider
) {
    private val autoDisposable = AutoDisposable()
    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    lateinit var tmdbResultsDto: Single<TmdbResultsDto>

    fun requestPageOfFilmsFromRemoteDataSource(page: Int, isOnSearch: Boolean) {
        var category = preferenceProvider.getFilmsCategory()
        if (isOnSearch)
            category = SEARCH_CATEGORY_NAME
        progressBarState.onNext(true)
        tmdbResultsDto = retrofitService.getFilms(category, API.KEY, "ru-RU", page)
        subscribeToPageOfFilms(category)
    }

    private fun subscribeToPageOfFilms(category: String) {
        tmdbResultsDto
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError {
                it.printStackTrace()
                progressBarState.onNext(false)
            }
            .subscribe({ dto ->
                dto.tmdbFilms.map {
                    Film(
                        it.id,
                        dto.page,
                        category,
                        it.title,
                        it.posterPath,
                        it.overview,
                        it.voteAverage
                    )
                }.also { putPageOfFilmsToLocal(it) }
                saveTotalPagesNumber(dto.totalPages)
                progressBarState.onNext(false)
            },
                {
                    it.printStackTrace()
                }).addTo(autoDisposable)

    }

    private fun putPageOfFilmsToLocal(list: List<Film>) {
        Completable.fromAction {
            repo.putPageOfFilms(list)
        }
            .subscribeOn(Schedulers.io())
            .subscribe({
                println("--------> Page of films successfully stored in local DB")
            },
                {
                    it.printStackTrace()
                }).addTo(autoDisposable)
    }

    fun requestPageOfFilmsFromLocalDataSource(page: Int): Observable<List<Film>> =
        repo.getPageOfFilmsInCategory(page, preferenceProvider.getFilmsCategory())

    fun clearLocalDataSource() = repo.deleteAll()

    fun getCurrentFilmsCategory() = preferenceProvider.getFilmsCategory()

    fun saveCurrentFilmsCategory(category: String) {
        preferenceProvider.saveFilmsCategory(category)
    }

    fun getTotalPagesNumber() =
        preferenceProvider.getTotalPagesNumber(getCurrentFilmsCategory())

    private fun saveTotalPagesNumber(totalPagesNumber: Int) {
        preferenceProvider.saveTotalPagesNumber(
            checkTotalPagesNumber(totalPagesNumber),
            getCurrentFilmsCategory()
        )
    }

    private fun checkTotalPagesNumber(num: Int?): Int {
        val default = PagesController.getDefaultTotalPagesByCategory(getCurrentFilmsCategory())
        return when (num) {
            !in PagesController.MIN_PAGES_NUM..PagesController.MAX_PAGES_NUM -> default
            else -> num ?: default
        }
    }

    fun getNightMode(): Int {
        return preferenceProvider.getNightModeSetting()
    }

    fun saveNightMode(mode: Int) {
        preferenceProvider.saveNightModeSetting(mode)
    }

    fun getLocalDataSourceUpdateTime() = preferenceProvider.getLocalDataSourceUpdateTime()

    private fun saveLocalDataSourceUpdateTime() {
        val dbUpdateTime = Calendar.getInstance().timeInMillis
        preferenceProvider.saveLocalDataSourceUpdateTime(dbUpdateTime)
    }

    fun registerPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferenceProvider.registerListener(listener)
    }

    fun unRegisterPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferenceProvider.unRegisterListener(listener)
    }

}

