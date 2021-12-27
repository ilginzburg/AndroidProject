package com.ginzburgworks.filmfinder.domain


import com.ginzburgworks.filmfinder.data.Film
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
                        page,
                        currentFilmsCategory
                    )
                    repo.putPageOfFilmsToDb(pageOfFilms)

                    response.body()?.totalPages?.let {
                        callback.onSuccess(
                            pageOfFilms,
                            totalPages = it
                        )
                    }
                }

                override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                    callback.onFailure()
                }
            })
    }

    fun getPageOfFilmsFromDB(page: Int): List<Film> =
        repo.getPageOfFilmsInCategoryFromDB(page, getFilmsCategoryFromPreferences())

    fun deleteDB() = repo.deleteDB()

    fun saveFilmsCategoryToPreferences(category: String) {
        preferenceProvider.saveFilmsCategory(category)
    }

    fun getFilmsCategoryFromPreferences() = preferenceProvider.getFilmsCategory()

    fun saveTotalPagesNumberToPreferences(TotalPagesNumber: Int,category:String) {
        preferenceProvider.saveTotalPagesNumber(TotalPagesNumber,category)
    }

    fun getTotalPagesNumberFromPreferences(category:String): Int{
        return preferenceProvider.getTotalPagesNumber(category)
    }

    fun saveUpdateDbTimeToPreferences(dbUpdateTime:Long){
        preferenceProvider.saveUpdateDbTime(dbUpdateTime)
    }

    fun getLastUpdateTimeFromPreferences() = preferenceProvider.getLasBDUpdateTime()

}

