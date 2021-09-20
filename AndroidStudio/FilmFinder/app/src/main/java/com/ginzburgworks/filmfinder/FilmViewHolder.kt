package com.ginzburgworks.filmfinder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.film_item.view.*

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title = itemView.title
    private val poster = itemView.poster
    private val description = itemView.description

    fun bind(film: Film) {
        title.text = film.title
        //poster.setImageResource(film.poster)
        //Указываем контейнер, в котором будет "жить" наша картинка
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(film.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(poster)
        description.text = film.description
    }
}