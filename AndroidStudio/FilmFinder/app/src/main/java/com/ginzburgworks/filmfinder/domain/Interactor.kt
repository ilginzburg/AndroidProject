package com.ginzburgworks.filmfinder.domain


import androidx.lifecycle.LiveData
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.PageManager
import com.ginzburgworks.filmfinder.data.PreferenceProvider
import com.ginzburgworks.filmfinder.data.TmdbApi
import com.ginzburgworks.filmfinder.data.db.MainRepository
import com.ginzburgworks.filmfinder.data.entity.TmdbResultsDto
import com.ginzburgworks.filmfinder.utils.API
import com.ginzburgworks.filmfinder.utils.Converter
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject


class Interactor @Inject constructor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    val preferenceProvider: PreferenceProvider
) {
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        val currentFilmsCategory = getFilmsCategoryFromPreferences()
        retrofitService.getFilms(currentFilmsCategory, API.KEY, "ru-RU", page)
            .enqueue(object : Callback<TmdbResultsDto> {
                override fun onResponse(
                    call: Call<TmdbResultsDto>,
                    response: Response<TmdbResultsDto>
                ) {
                    val pageOfFilms = Converter.convertApiListToDtoList(
                        response.body()?.tmdbFilms,
                        response.body()?.page,
                        currentFilmsCategory
                    )
                    repo.putPageOfFilmsToDb(pageOfFilms)
                    saveUpdateDbTime()
                    saveTotalPagesNumber(response.body()?.totalPages)
                    callback.onSuccess()

                }

                override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                    callback.onFailure()
                }
            })
    }

    private fun saveUpdateDbTime() {
        val dbUpdateTime = Calendar.getInstance().timeInMillis
        saveUpdateDbTimeToPreferences(dbUpdateTime)
    }


    fun getPageOfFilmsFromDB(page: Int): LiveData<List<Film>> =
        repo.getPageOfFilmsInCategoryFromDB(page, getFilmsCategoryFromPreferences())


    fun deleteDB() = repo.deleteDB()

    fun saveFilmsCategoryToPreferences(category: String) {
        preferenceProvider.saveFilmsCategory(category)
    }

    fun getFilmsCategoryFromPreferences() = preferenceProvider.getFilmsCategory()

    fun saveTotalPagesNumberToPreferences(TotalPagesNumber: Int, category: String) {
        preferenceProvider.saveTotalPagesNumber(TotalPagesNumber, category)
    }

    fun getTotalPagesNumberFromPreferences(category: String): Int {
        return preferenceProvider.getTotalPagesNumber(category)
    }

    fun getLastUpdateTimeFromPreferences() = preferenceProvider.getLasBDUpdateTime()

    private fun saveUpdateDbTimeToPreferences(dbUpdateTime: Long) {
        preferenceProvider.saveUpdateDbTime(dbUpdateTime)
    }

    private fun saveTotalPagesNumber(totalPagesNumber: Int?) {
        if (totalPagesNumber ?: 0 == 0)
            return
        var totalPagesFromNetwork = PageManager.MAX_PAGES_NUM
        if (totalPagesNumber!! < PageManager.MAX_PAGES_NUM)
            totalPagesFromNetwork = totalPagesNumber
        saveTotalPagesNumberToPreferences(
            totalPagesFromNetwork,
            getFilmsCategoryFromPreferences()
        )
    }

}

