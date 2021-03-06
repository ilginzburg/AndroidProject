package com.ginzburgworks.filmfinder.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.domain.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.net.UnknownServiceException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class DetailsFragmentViewModel : ViewModel() {

    private val exceptionHandler =
        CoroutineExceptionHandler { _, e -> errorEvent.postValue(App.instance.getString(R.string.exc_handler_msg) + e) }
    private val detailsFragmentViewModelContext =
        viewModelScope.coroutineContext.plus(exceptionHandler + Dispatchers.IO)
    val detailsFragmentViewModelScope = CoroutineScope(detailsFragmentViewModelContext)
    val errorEvent = SingleLiveEvent<String>()

    suspend fun loadWallpaper(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        return suspendCoroutine { continuation ->
            provideInputStream(url)?.let { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.let {
                    bitmap = it
                } ?: postErrorMessage(R.string.bitmap_err_msg)
            }
            continuation.resume(bitmap)
        }
    }

    private fun provideInputStream(url: String): InputStream? {
        val urlFormed = createURL(url) ?: return null
        val connection = establishConnection(urlFormed) ?: return null
        return requestInputStream(connection)
    }

    private fun requestInputStream(urlConnectionInstance: URLConnection): InputStream? {
        return try {
            urlConnectionInstance.getInputStream()
        } catch (e: IOException) {
            postErrorMessage(R.string.network_err_msg)
            null
        } catch (e: UnknownServiceException) {
            postErrorMessage(R.string.unknown_service_err_msg)
            null
        }
    }

    private fun establishConnection(url: URL): URLConnection? {
        return try {
            url.openConnection()
        } catch (e: IOException) {
            postErrorMessage(R.string.network_err_msg)
            null
        }
    }

    private fun createURL(url: String): URL? {
        return try {
            URL(url)
        } catch (e: MalformedURLException) {
            postErrorMessage(R.string.url_err_msg)
            null
        }
    }

    fun postErrorMessage(msgID: Int) {
        errorEvent.postValue(App.instance.getString(msgID))
    }

}