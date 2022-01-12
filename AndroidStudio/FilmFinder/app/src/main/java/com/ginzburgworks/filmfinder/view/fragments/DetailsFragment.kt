package com.ginzburgworks.filmfinder.view.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.local.DefaultFilm
import com.ginzburgworks.filmfinder.data.local.Favorites
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.remote.ApiConstants
import com.ginzburgworks.filmfinder.databinding.FragmentDetailsBinding
import com.ginzburgworks.filmfinder.domain.GalleryInteractor
import com.ginzburgworks.filmfinder.domain.PermissionHandler
import com.ginzburgworks.filmfinder.viewmodels.DetailsFragmentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

const val KEY_FILM = "film"
private const val DETAILS_FRAG_IMG_SIZE = "w780"
private const val TYPE_OF_SHARE_INTENT = "text/plain"
private const val TYPE_OF_VIEW_INTENT = "image/*"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var galleryInteractor: GalleryInteractor
    private lateinit var permissionHandler: PermissionHandler

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[DetailsFragmentViewModel::class.java]
    }

    @Singleton
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        bindComponents()
        subscribeToErrorMessages()
    }

    private fun bindComponents() {
        val film = arguments?.get(KEY_FILM) as Film
        binding.film = film
        binding.detailsFragment = this
    }

    private fun subscribeToErrorMessages() {
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    fun toggleFavorites(film: Film) {
        if (!film.isInFavorites)
            addToFavorites(film)
        else
            removeFromFavorites(film)
        loadIcon(
            binding.detailsFabFavorites,
            binding.detailsFabFavorites.drawable
        )
        binding.invalidateAll()
    }

    private fun addToFavorites(film: Film) {
        film.isInFavorites = true
        Favorites.favoritesList.add(film)
    }

    private fun removeFromFavorites(film: Film) {
        film.isInFavorites = false
        Favorites.favoritesList.remove(film)
    }

    fun openShareDialog(film: Film) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(
                Intent.EXTRA_TEXT,
                getIntentExtraText(film)
            )
            type = TYPE_OF_SHARE_INTENT
            startActivity(Intent.createChooser(this, App.instance.getString(R.string.share_title)))
        }
    }

    private fun getIntentExtraText(film: Film): String =
        App.instance.getString(R.string.share_msg) + "  " + film.title + "\n" + film.description

    fun performAsyncLoadOfPoster(film: Film) {
        permissionHandler = PermissionHandler()
        galleryInteractor = GalleryInteractor()
        if (!permissionHandler.checkPermission(requireContext())) {
            permissionHandler.requestPermission(requireActivity())
            return
        }
        viewModel.viewModelScope.launch {
            binding.progressBar.isVisible = true
            val job = viewModel.detailsFragmentViewModelScope.async {
                viewModel.loadWallpaper(ApiConstants.IMAGES_URL + "original" + film.poster)
            }
            job.await()?.let {
                galleryInteractor.saveImageToGallery(
                    it,
                    film,
                    requireActivity(),
                    viewModel
                )
                initSnackBar()
            }
            binding.progressBar.isVisible = false
        }
    }

    private fun initSnackBar() {
        Snackbar.make(
            binding.root,
            R.string.downloaded_to_gallery,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.open) {
            Intent(Intent.ACTION_VIEW).apply {
                type = TYPE_OF_VIEW_INTENT
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }
            .show()
    }

    companion object {

        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(view: AppCompatImageView, imageUrl: String) {
            val sourceImageUrl = ApiConstants.IMAGES_URL + "w780" + imageUrl
            val defaultImage = DefaultFilm.film.poster
            Glide.with(view)
                .load(sourceImageUrl)
                .centerCrop()
                .error(defaultImage)
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("srcCompat")
        fun loadIcon(view: FloatingActionButton, icon: Drawable) {
            view.setImageDrawable(icon)
        }
    }
}