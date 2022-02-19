package com.ginzburgworks.filmfinder.domain

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.viewmodels.DetailsFragmentViewModel
import com.ginzburgworks.local_module.Film
import java.io.FileNotFoundException
import java.io.OutputStream


private const val COMPRESS_FACTOR = 100
private const val MS_TO_SEC_FACTOR = 1000
private const val APP_GALLERY_RELATIVE_PATH = "Pictures/FilmFinderApp"
private const val MEDIA_MIME_TYPE = "image/jpeg"

class GalleryInteractor {
    fun saveImageToGallery(
        bitmap: Bitmap, film: Film, activity: Activity, viewModel: DetailsFragmentViewModel
    ) {
        val contentResolver = activity.contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.DISPLAY_NAME, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.MIME_TYPE, MEDIA_MIME_TYPE)
                put(
                    MediaStore.Images.Media.DATE_ADDED,
                    System.currentTimeMillis() / MS_TO_SEC_FACTOR
                )
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, APP_GALLERY_RELATIVE_PATH)
            }.also { values ->
                contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )?.let { uri ->
                    requestOutputStream(uri, contentResolver, viewModel)?.let {
                        if (!bitmap.compress(
                                Bitmap.CompressFormat.JPEG, COMPRESS_FACTOR, it
                            )
                        ) viewModel.postErrorMessage(R.string.bitmap_compress_err_msg)
                        it.close()
                    }
                } ?: viewModel.postErrorMessage(R.string.uri_err_msg)
            }
        } else {
            @Suppress("DEPRECATION") MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                film.title.handleSingleQuote(),
                film.description.handleSingleQuote()
            )
        }
    }

    private fun requestOutputStream(
        uri: Uri, contentResolver: ContentResolver, viewModel: DetailsFragmentViewModel
    ): OutputStream? {
        return try {
            contentResolver.openOutputStream(uri)
        } catch (e: FileNotFoundException) {
            viewModel.postErrorMessage(R.string.output_stream_err_msg)
            null
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }

}