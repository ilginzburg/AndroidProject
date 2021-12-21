package com.ginzburgworks.filmfinder.view.rv_viewholders

import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.ApiConstants
import com.ginzburgworks.filmfinder.databinding.FilmItemBinding
import com.ginzburgworks.filmfinder.domain.Film

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val filmItemBinding: FilmItemBinding? = DataBindingUtil.bind(itemView)

    fun bind(film: Film) {
        filmItemBinding?.film = film
        val fullUrl = ApiConstants.IMAGES_URL + "w342" + film.poster
        if (filmItemBinding != null) {
            loadImage(filmItemBinding.poster, itemView, fullUrl)
        }
    }

    private fun loadImage(
        posterView: ImageView,
        itemView: View,
        posterUrl: String
    ) {
        val defaultImage = R.drawable.tv_default
        Glide.with(itemView)
            .load(posterUrl)
            .centerCrop()
            .error(defaultImage)
            .into(posterView)

    }
}