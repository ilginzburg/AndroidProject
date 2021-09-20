package com.ginzburgworks.filmfinder

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_home.*


class FavoritesFragment : Fragment() {


    init {
        enterTransition = Slide(Gravity.END).apply { duration = 800 }
        reenterTransition = Slide(Gravity.START).apply { duration = 800; }
        returnTransition = Slide(Gravity.END).apply { duration = 800;mode = Slide.MODE_OUT }
        exitTransition = Slide(Gravity.START).apply { duration = 800;mode = Slide.MODE_OUT }
    }

    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Получаем список при транзакции фрагмента


        favorites_recycler
            .apply {
                filmsAdapter =
                    FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                        override fun click(film: Film) {
                            (requireActivity() as MainActivity).launchDetailsFragment(film)
                        }
                    })
                //Присваиваем адаптер
                adapter = filmsAdapter
                //Присвои layoutmanager
                layoutManager = LinearLayoutManager(requireContext())
                //Применяем декоратор для отступов
                val decorator = TopSpacingItemDecoration(8)
                addItemDecoration(decorator)
            }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(Favorites.favoritesList)

    }
}