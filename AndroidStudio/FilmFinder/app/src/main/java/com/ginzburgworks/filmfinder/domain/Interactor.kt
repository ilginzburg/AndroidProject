package com.ginzburgworks.filmfinder.domain

import com.ginzburgworks.filmfinder.data.TmdbApi
import com.ginzburgworks.filmfinder.data.entity.TmdbResultsDto
import com.ginzburgworks.filmfinder.utils.API
import com.ginzburgworks.filmfinder.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val retrofitService: TmdbApi) {
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(
                call: Call<TmdbResultsDto>,
                response: Response<TmdbResultsDto>
            ) {
                callback.onSuccess(
                    com.ginzburgworks.filmfinder.utils.Converter.convertApiListToDtoList(
                        response.body()?.tmdbFilms
                    )
                )
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                callback.onFailure()
            }
        })
    }
}