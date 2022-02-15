package com.ginzburgworks.filmfinder.view.rv_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.databinding.FilmItemBinding
import com.ginzburgworks.filmfinder.view.rv_viewholders.FilmViewHolder
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel

class FilmListRecyclerAdapter : RecyclerView.Adapter<FilmViewHolder>() {

    private lateinit var clickListener: OnItemClickListener

    private val items = mutableListOf<Film>()

    private lateinit var filmItemBinding: FilmItemBinding

    fun setListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        filmItemBinding = FilmItemBinding.inflate(inflater, parent, false)
        return FilmViewHolder(filmItemBinding.root)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
        filmItemBinding.itemContainer.setOnClickListener { clickListener.onClick(items[holder.adapterPosition]) }
    }

    fun addItems(list: List<Film>) {
        val itemCountBeforeAdding = itemCount
        val itemsAdded = list.size
        items.addAll(list)
        notifyItemRangeInserted(itemCountBeforeAdding, itemsAdded)
        notifyItemRangeChanged(0, itemsAdded)
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

    fun interface OnItemClickListener {

        fun onClick(film: Film)
    }
}