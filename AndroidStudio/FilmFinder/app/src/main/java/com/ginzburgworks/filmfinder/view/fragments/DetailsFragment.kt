package com.ginzburgworks.filmfinder.view.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.ApiConstants
import com.ginzburgworks.filmfinder.data.Favorites
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.databinding.FragmentDetailsBinding
import com.ginzburgworks.filmfinder.utils.Converter
import com.ginzburgworks.filmfinder.viewmodels.DetailsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton



class DetailsFragment : Fragment() {

    private lateinit var fragmentDetailsBinding: FragmentDetailsBinding
    private val scope = CoroutineScope(Dispatchers.IO)

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
        initDownloadButton(film)
        subscribeToErrorMessages()
    }

    private fun subscribeToErrorMessages() {
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initDownloadButton(film: Film) {
        fragmentDetailsBinding.detailsFabDownloadWp.setOnClickListener {
            performAsyncLoadOfPoster(film)
        }
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
            type = TYPE_OF_SHARE_INTENT
            startActivity(Intent.createChooser(this, App.instance.getString(R.string.share_title)))
        }
    }

    private fun getIntentExtraText(film: Film): String =
        "${R.string.share_msg} ${film.title} \n ${film.description}"

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


    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

    private fun saveToGallery(bitmap: Bitmap,film: Film) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, film.title.handleSingleQuote())
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    film.title.handleSingleQuote()
                )
                put(MediaStore.Images.Media.MIME_TYPE, MEDIA_MIME_TYPE)
                put(
                    MediaStore.Images.Media.DATE_ADDED,
                    System.currentTimeMillis() / 1000
                )
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, APP_GALLERY_RELATIVE_PATH)
            }
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            val outputStream = contentResolver.openOutputStream(uri!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, outputStream)
            outputStream?.close()
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                film.title.handleSingleQuote(),
                film.description.handleSingleQuote()
            )
        }
    }


    private fun performAsyncLoadOfPoster(film: Film) {
        if (!checkPermission()) {
            requestPermission()
            return
        }
        MainScope().launch {
            fragmentDetailsBinding.progressBar.isVisible = true
            val job = scope.async {
                viewModel.loadWallpaper(ApiConstants.IMAGES_URL + "original" + film.poster)
            }
            val bitmap = job.await()
            bitmap?.let {
                saveToGallery(it, film)
                Snackbar.make(
                    fragmentDetailsBinding.root,
                    R.string.downloaded_to_gallery,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.open) {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.type = TYPE_OF_VIEW_INTENT
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
            fragmentDetailsBinding.progressBar.isVisible = false
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }


    companion object {
        const val KEY_FILM = "film"
        private const val DETAILS_FRAG_IMG_SIZE = "w780"
        private const val TYPE_OF_SHARE_INTENT = "text/plain"
        private const val TYPE_OF_VIEW_INTENT = "image/*"
        private const val BITMAP_COMPRESS_QUALITY = 100
        private const val APP_GALLERY_RELATIVE_PATH = "Pictures/FilmFinderApp"
        private const val MEDIA_MIME_TYPE = "image/jpeg"
    }
}