package com.ginzburgworks.filmfinder.domain

import com.ginzburgworks.filmfinder.data.MainRepository

class Interactor(val repo: MainRepository) {
    fun getFilmsDB(): List<Film> = repo.filmsDataBase
}