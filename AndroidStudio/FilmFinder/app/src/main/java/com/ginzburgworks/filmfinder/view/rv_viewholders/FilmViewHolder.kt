package com.ginzburgworks.filmfinder.view.rv_viewholders

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ginzburgworks.filmfinder.data.ApiConstants
import com.ginzburgworks.filmfinder.databinding.FilmItemBinding
import com.ginzburgworks.filmfinder.domain.Film

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding: FilmItemBinding = DataBindingUtil.bind(itemView)!!

    fun bind(film: Film) {
        binding.film = film
        Glide.with(itemView)
            .load(ApiConstants.IMAGES_URL + "w342" + film.poster)
            .centerCrop()
            .into(binding.poster)
    }
}