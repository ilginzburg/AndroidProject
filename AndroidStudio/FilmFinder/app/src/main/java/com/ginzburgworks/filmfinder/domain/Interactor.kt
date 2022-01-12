package com.ginzburgworks.filmfinder.domain

import android.content.SharedPreferences
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.db.FilmsRepository
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.data.remote.API
import com.ginzburgworks.filmfinder.data.remote.TmdbApi
import com.ginzburgworks.filmfinder.data.remote.entity.TmdbResultsDto
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.MAX_PAGES_NUM
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.MIN_PAGES_NUM
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import java.util.*
import javax.inject.Inject

class Interactor @Inject constructor(
    private val repo: FilmsRepository,
    private val retrofitService: TmdbApi,
    private val preferenceProvider: PreferenceProvider

) {


    val pageFromRemote = Channel<List<Film>>(Channel.CONFLATED)
    val pageFromLocal = Channel<List<Film>>(Channel.CONFLATED)

    suspend fun getPageOfFilmsFromRemoteDataSource(page: Int) = coroutineScope {
        val currentFilmsCategory = getCurrentFilmsCategory()
        val result = async {
            retrofitService.getFilms(currentFilmsCategory, API.KEY, "ru-RU", page)
        }
        result.await()?.let { dto ->
            convertToLocalDataStorageForm(dto, currentFilmsCategory)
                .let { pageOfFilms ->
                    repo.putPageOfFilms(pageOfFilms)
                    pageFromRemote.send(pageOfFilms)
                }
            saveTotalPagesNumber(dto.totalPages)
            saveLocalDataSourceUpdateTime()

        }
    }

    private fun convertToLocalDataStorageForm(
        tmdb: TmdbResultsDto,
        currentFilmsCategory: String
    ): List<Film> {
        return Converter.convertTmdbFilmListToFilmList(
            tmdb.tmdbFilms,
            tmdb.page,
            currentFilmsCategory
        )
    }


    suspend fun getPageOfFilmsFromLocalDataSourceToUI(page: Int) = coroutineScope {
        val pageOfFilms = repo.getPageOfFilmsInCategory(page, getCurrentFilmsCategory())
        pageFromLocal.send(pageOfFilms)
    }

    suspend fun clearLocalDataSource() = repo.deleteAll()

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

    fun getLocalDataSourceUpdateTime() = preferenceProvider.getLocalDataSourceUpdateTime()

    private fun saveLocalDataSourceUpdateTime() {
        val dbUpdateTime = Calendar.getInstance().timeInMillis
        preferenceProvider.saveLocalDataSourceUpdateTime(dbUpdateTime)
    }

    private fun checkTotalPagesNumber(num: Int): Int {
        return when (num) {
            !in MIN_PAGES_NUM..MAX_PAGES_NUM -> PagesController.getDefaultTotalPagesByCategory(
                getCurrentFilmsCategory()
            )
            else -> num
        }
    }

    fun registerPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferenceProvider.registerListener(listener)
    }

    fun unRegisterPreferencesListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferenceProvider.unRegisterListener(listener)
    }

    fun getNightMode(): Int {
        return preferenceProvider.getNightModeSetting()
    }

    fun saveNightMode(mode: Int) {
        preferenceProvider.saveNightModeSetting(mode)
    }

}

