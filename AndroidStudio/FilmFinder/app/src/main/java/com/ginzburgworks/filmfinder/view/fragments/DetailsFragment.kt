package com.ginzburgworks.filmfinder.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.ApiConstants
import com.ginzburgworks.filmfinder.data.Favorites
import com.ginzburgworks.filmfinder.databinding.FragmentDetailsBinding
import com.ginzburgworks.filmfinder.domain.Film

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film = arguments?.get(KEY_FILM) as Film
        binding.titleText = film.title
        loadImage(film.poster, binding.detailsPoster)
        binding.descriptionText = film.description
        binding.detailsFabFavorites.setImageResource(
            if (film.isInFavorites) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )

        binding.detailsFabFavorites.setOnClickListener {
            if (!film.isInFavorites) {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                film.isInFavorites = true
                Favorites.favoritesList.add(film)
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                film.isInFavorites = false
                Favorites.favoritesList.remove(film)
            }
        }

        binding.detailsFabShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    private fun loadImage(posterUrl: String, posterView: ImageView) {
        val defaultImage = R.drawable.tv_default
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + posterUrl)
            .centerCrop()
            .error(defaultImage)
            .into(posterView)
    }

    companion object {
        const val KEY_FILM = "film"
    }
}