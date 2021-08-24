package com.ginzburgworks.filmfinder

import androidx.recyclerview.widget.DiffUtil

class FilmDiff (val oldList: MutableList <Film> , val newList: MutableList <Film> ): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    //Элементы одинаковые
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    //Содержимое одинаковое
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.title == newProduct.title &&
                oldProduct.poster == newProduct.poster &&
                oldProduct.description == newProduct.description
    }
}