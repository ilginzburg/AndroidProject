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
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.databinding.FragmentDetailsBinding
import com.ginzburgworks.filmfinder.utils.Converter

class DetailsFragment : Fragment() {

    private lateinit var fragmentDetailsBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDetailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return fragmentDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        val film = arguments?.get(KEY_FILM) as Film
        initPoster(film)
        initTitle(film)
        initDescription(film)
        initFavoritesButton(film)
        initShareButton(film)
    }

    private fun initPoster(film: Film) {
        loadImage(film.poster, fragmentDetailsBinding.detailsPoster)
    }

    private fun initTitle(film: Film) {
        fragmentDetailsBinding.titleText = film.title
    }

    private fun initDescription(film: Film) {
        fragmentDetailsBinding.descriptionText = film.description
    }

    private fun initFavoritesButton(film: Film) {
        setFavoritesIcon(film)
        fragmentDetailsBinding.detailsFabFavorites.setOnClickListener {
            toggleFavorites(film)
        }
    }

    private fun toggleFavorites(film: Film) {
        if (!film.isInFavorites)
            addToFavorites(film)
        else
            removeFromFavorites(film)
        setFavoritesIcon(film)
    }

    private fun addToFavorites(film: Film) {
        film.isInFavorites = true
        Favorites.favoritesList.add(film)
    }

    private fun removeFromFavorites(film: Film) {
        film.isInFavorites = false
        Favorites.favoritesList.remove(film)
    }

    private fun initShareButton(film: Film) {
        fragmentDetailsBinding.detailsFabShare.setOnClickListener {
            openShareDialog(film)
        }
    }

    private fun openShareDialog(film: Film) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(
                Intent.EXTRA_TEXT,
                getIntentExtraText(film)
            )
            type = TYPE_OF_INTENT_TO_SHARE
            startActivity(Intent.createChooser(this, TITLE_OF_INTENT_TO_SHARE))
        }
    }

    private fun getIntentExtraText(film: Film): String =
        "$MSG_INTENT_TO_SHARE ${film.title} \n ${film.description}"

    private fun loadImage(posterUrl: String, posterView: ImageView) {
        val sourceImageUrl = ApiConstants.IMAGES_URL + DETAILS_FRAG_IMG_SIZE + posterUrl
        val defaultImage = Converter.DefaultFilm.film.poster
        Glide.with(this)
            .load(sourceImageUrl)
            .centerCrop()
            .error(defaultImage)
            .into(posterView)
    }

    private fun setFavoritesIcon(film: Film) {
        if (film.isInFavorites) setAddedToFavoritesIcon()
        else removeAddedToFavoritesIcon()
    }

    private fun setAddedToFavoritesIcon() {
        fragmentDetailsBinding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
    }

    private fun removeAddedToFavoritesIcon() {
        fragmentDetailsBinding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }

    companion object {
        const val KEY_FILM = "film"
        private const val DETAILS_FRAG_IMG_SIZE = "w780"
        private const val MSG_INTENT_TO_SHARE = "Посмотрите этот фильм: "
        private const val TYPE_OF_INTENT_TO_SHARE = "text/plain"
        private const val TITLE_OF_INTENT_TO_SHARE = "Поделиться: "
    }
}