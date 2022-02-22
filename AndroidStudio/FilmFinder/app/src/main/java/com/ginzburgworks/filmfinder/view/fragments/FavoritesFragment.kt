package com.ginzburgworks.filmfinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initAnimation()
    }

    private fun initRecycler() {
        binding.favoritesRecycler.apply {
            filmsAdapter = FilmListRecyclerAdapter()
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addDecoration(this)
        }
        filmsAdapter.addItems(Favorites.favoritesList)
        filmsAdapter.onItemClick = { launchDetailsFragment(it) }
    }

    private fun launchDetailsFragment(film: Film) {
        (requireActivity() as MainActivity).launchDetailsFragment(film)
    }

    private fun addDecoration(recycler: RecyclerView) {
        val decorator = TopSpacingItemDecoration(DECORATOR_PADDING)
        recycler.addItemDecoration(decorator)
    }

    private fun initAnimation() {
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.favoritesFragmentRoot, requireActivity(), ANIM_POSITION
        )
    }
}

