package com.ginzburgworks.filmfinder.view.rv_viewholders

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ginzburgworks.filmfinder.databinding.FilmItemBinding
import com.ginzburgworks.filmfinder.domain.Film
import com.ginzburgworks.filmfinder.view.customviews.RatingDonutView


class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding: FilmItemBinding = DataBindingUtil.bind(itemView)!!

    fun bind(film: Film) {
        binding.film = film
        Glide.with(itemView)
            .load(film.poster)
            .centerCrop()
            .into(binding.poster)
        binding.ratingDonut.setProgress((film.rating * RatingDonutView.RATING_FACTOR).toInt())
    }
}