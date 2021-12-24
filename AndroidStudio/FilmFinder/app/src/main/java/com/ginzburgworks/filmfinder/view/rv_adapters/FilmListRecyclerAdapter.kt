package com.ginzburgworks.filmfinder.view.rv_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ginzburgworks.filmfinder.databinding.FilmItemBinding
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.view.rv_viewholders.FilmViewHolder
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel


class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<FilmViewHolder>() {

    private val items = mutableListOf<Film>()
    private lateinit var filmItemBinding: FilmItemBinding

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        filmItemBinding = FilmItemBinding.inflate(inflater, parent, false)
        return FilmViewHolder(filmItemBinding.root)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(items[position])
          filmItemBinding.itemContainer.setOnClickListener {
                clickListener.onClick(items[position])
          }
    }

    fun addItems(list: List<Film>) {
        val itemCountBeforeAdding = itemCount
        val itemsAdded = list.size
        items.addAll(list)
        notifyItemRangeInserted(itemCountBeforeAdding, itemsAdded)
        notifyItemRangeChanged(0, itemCount)
    }

    fun clearItems() {
        val itemCountBeforeClear = itemCount
        items.clear()
        notifyItemRangeRemoved(0, itemCountBeforeClear)
    }

    fun saveItemsForSearch(viewModel: HomeFragmentViewModel) {
        viewModel.itemsForSearch.clear()
        viewModel.itemsForSearch.addAll(items)
    }

    interface OnItemClickListener {
        fun onClick(film: Film)
    }
}