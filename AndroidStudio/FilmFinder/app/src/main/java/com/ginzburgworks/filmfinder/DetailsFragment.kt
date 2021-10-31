package com.ginzburgworks.filmfinder

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Slide
import kotlinx.android.synthetic.main.fragment_details.*

private const val KEY_FILM = "film"

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film = arguments?.get(KEY_FILM) as Film
        details_toolbar.title = film?.title ?: "none"
        details_poster.setImageResource(film?.poster ?: (R.drawable.corner_background))
        details_description.text = film?.description ?: "none"

        details_fab_favorites.setImageResource(
            if (film.isInFavorites) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )

        details_fab_favorites.setOnClickListener {
            if (!film.isInFavorites) {
                details_fab_favorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                film.isInFavorites = true
                Favorites.favoritesList.add(film)
            } else {
                details_fab_favorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                film.isInFavorites = false
                Favorites.favoritesList.remove(film)
            }
        }

        details_fab_share.setOnClickListener {
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
}