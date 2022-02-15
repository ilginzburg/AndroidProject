package com.ginzburgworks.filmfinder.domain

import android.content.SharedPreferences
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.data.remote.API
import com.ginzburgworks.filmfinder.data.remote.TmdbApi
import com.ginzburgworks.filmfinder.data.remote.entity.TmdbResultsDto
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*

class Interactor(
    private val repo: FilmsRepository,
    private val retrofitService: TmdbApi,
    private val preferenceProvider: PreferenceProvider
) {

    val pageFromDataSourceToUI = Channel<List<Film>>(Channel.CONFLATED)
    val progressBarScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    var progressBarState = Channel<Boolean>(Channel.CONFLATED)

    suspend fun requestPageOfFilmsFromRemoteDataSource(page: Int) = coroutineScope {
        val category = preferenceProvider.getFilmsCategory()
        progressBarScope.launch {
            progressBarState.send(true)
        }
        val result = async {
            retrofitService.getFilms(category, API.KEY, "ru-RU", page)
        }
        result.await()?.let { dto ->
            launch {
                getConvertedDTO(dto, category).collect { pageOfFilms ->
                    sendPageOfFilmsToView(pageOfFilms)
                    repo.putPageOfFilms(pageOfFilms)
                }
                saveTotalPagesNumber(dto.totalPages)
                saveLocalDataSourceUpdateTime()
            }
        }
    }

    private suspend fun getConvertedDTO(tmdb: TmdbResultsDto, category: String) = flow {
        val list = tmdb.tmdbFilms.map {
            Film(
                it.id, tmdb.page, category, it.title, it.posterPath, it.overview, it.voteAverage
            )
        }
        emit(list)
    }

    private suspend fun sendPageOfFilmsToView(pageOfFilms: List<Film>) {
        pageFromDataSourceToUI.send(pageOfFilms)
        progressBarScope.launch {
            progressBarState.send(false)
        }
    }

    suspend fun requestPageOfFilmsFromLocalDataSource(page: Int) {
        progressBarScope.launch {
            progressBarState.send(true)
        }
        val pageOfFilms = repo.getPageOfFilmsInCategory(page, preferenceProvider.getFilmsCategory())
        if (pageOfFilms.isEmpty()) requestPageOfFilmsFromRemoteDataSource(page)
        else sendPageOfFilmsToView(pageOfFilms)
    }

    suspend fun clearLocalDataSource() = repo.deleteAll()

    fun getCurrentFilmsCategory() = preferenceProvider.getFilmsCategory()

    fun saveCurrentFilmsCategory(category: String) {
        preferenceProvider.saveFilmsCategory(category)
    }

    fun getTotalPagesNumber() = preferenceProvider.getTotalPagesNumber(getCurrentFilmsCategory())

    private fun saveTotalPagesNumber(totalPagesNumber: Int) {
        preferenceProvider.saveTotalPagesNumber(
            checkTotalPagesNumber(totalPagesNumber), getCurrentFilmsCategory()
        )
    }

    private fun checkTotalPagesNumber(num: Int): Int {
        return when (num) {
            !in PagesController.MIN_PAGES_NUM..PagesController.MAX_PAGES_NUM -> PagesController.getDefaultTotalPagesByCategory(
                getCurrentFilmsCategory()
            )
            else -> num
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

