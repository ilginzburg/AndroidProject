package com.ginzburgworks.filmfinder.data

import com.ginzburgworks.filmfinder.domain.Film

class Favorites {

    companion object {
        val favoritesList: MutableList<Film> = mutableListOf()
    }
}