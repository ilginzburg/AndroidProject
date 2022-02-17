package com.ginzburgworks.filmfinder.view.rv_viewholders

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ginzburgworks.filmfinder.data.local.DefaultFilm
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.remote_module.entity.ApiConstants
import com.ginzburgworks.filmfinder.databinding.FilmItemBinding

private const val VIEW_HOLDER_IMG_SIZE = "w342"

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val filmItemBinding: FilmItemBinding by lazy { getThisFilmItemBinding() }

    private fun getThisFilmItemBinding(): FilmItemBinding {
        return DataBindingUtil.bind(itemView) ?: FilmItemBinding.inflate(
            LayoutInflater.from(
                itemView.context
            )
        )
    }

    fun bind(film: Film) {
        filmItemBinding.film = film
        loadImage(filmItemBinding.poster, itemView, film.poster)
    }

    private fun loadImage(posterView: ImageView, itemView: View, posterUrl: String) {
        val sourceImageUrl = com.ginzburgworks.remote_module.entity.ApiConstants.IMAGES_URL + VIEW_HOLDER_IMG_SIZE + posterUrl
        val defaultImage = DefaultFilm.film.poster
        Glide.with(itemView)
            .load(sourceImageUrl)
            .centerCrop()
            .error(defaultImage)
            .into(posterView)
    }

}