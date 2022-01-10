package com.ginzburgworks.filmfinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ginzburgworks.filmfinder.data.local.Favorites
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.databinding.FragmentFavoritesBinding
import com.ginzburgworks.filmfinder.utils.AnimationHelper
import com.ginzburgworks.filmfinder.utils.TopSpacingItemDecoration
import com.ginzburgworks.filmfinder.view.MainActivity
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter

private const val DECORATOR_PADDING = 8
private const val ANIM_POSITION = 4

class FavoritesFragment : Fragment() {

    private lateinit var fragmentFavoritesBinding: FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return fragmentFavoritesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initAnimation()
    }

    private fun initRecycler() {
        fragmentFavoritesBinding.favoritesRecycler
            .apply {
                filmsAdapter = FilmListRecyclerAdapter()
                adapter = filmsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                val decorator = TopSpacingItemDecoration(DECORATOR_PADDING)
                addItemDecoration(decorator)
            }
        filmsAdapter.addItems(Favorites.favoritesList)
        filmsAdapter.setListener(object : FilmListRecyclerAdapter.OnItemClickListener {
            override fun onClick(film: Film) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })
    }

    private fun initAnimation() {
        AnimationHelper.performFragmentCircularRevealAnimation(
            fragmentFavoritesBinding.favoritesFragmentRoot,
            requireActivity(),
            ANIM_POSITION
        )
    }
}

