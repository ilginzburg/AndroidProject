package com.ginzburgworks.filmfinder.domain

import android.content.SharedPreferences
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

class Interactor(
    private val repo: FilmsRepository,
    private val retrofitService: TmdbApi,
    private val preferenceProvider: PreferenceProvider
) {

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    lateinit var remoteResult: Single<TmdbResultsDto>

    fun requestPageOfFilmsFromRemoteDataSource(page: Int) {
        val category = preferenceProvider.getFilmsCategory()
        progressBarState.onNext(true)
        remoteResult = retrofitService.getFilms(category, API.KEY, "ru-RU", page)
        subscribeToPageOfFilms(category)
    }

    private fun subscribeToPageOfFilms(category: String) {
        remoteResult
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError {
                println("-------------->doOnError")
                println("----------> !!! error: $it")
                progressBarState.onNext(false)
            }
            .doOnSuccess { dto ->
                println("-------------->doOnSuccess")
                val list = dto.tmdbFilms.map {
                    Film(
                        it.id,
                        dto.page,
                        category,
                        it.title,
                        it.posterPath,
                        it.overview,
                        it.voteAverage
                    )
                }
                putPageOfFilmsToLocal(list)
                saveTotalPagesNumber(dto.totalPages)
                progressBarState.onNext(false)
            }
            .doOnTerminate {
                println("-------> doOnTerminate")

            }
            .subscribe()
    }

    private fun putPageOfFilmsToLocal(list: List<Film>) {
        Completable.fromAction {
            repo.putPageOfFilms(list)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
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

