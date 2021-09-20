package com.ginzburgworks.filmfinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.film_item.view.*

class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<Film>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                holder.bind(items[position])
                holder.itemView.item_container.setOnClickListener {
                    clickListener.click(items[position])
                }
            }
        }
    }

    //Метод для добавления объектов в наш список
    fun addItems(list: List<Film>) {
        //Сначала очишаем(если не реализовать DiffUtils)
        items.clear()
        //Добавляем
        items.addAll(list)
        //Уведомляем RV, что пришел новый список и ему нужно заново все "привязывать"
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun click(film: Film)
    }
}