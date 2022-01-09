package com.ginzburgworks.filmfinder.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.SingleLiveEvent
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.net.UnknownServiceException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class DetailsFragmentViewModel @Inject constructor() : ViewModel() {

    val errorEvent = SingleLiveEvent<String>()

    suspend fun loadWallpaper(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        return suspendCoroutine {
            val urlFormed = createURL(url)
            urlFormed?.apply {
                val urlConnectionInstance = establishConnection(this)
                urlConnectionInstance?.apply {
                    val inputStream = requestInputStream(this)
                    inputStream?.apply {
                        bitmap = BitmapFactory.decodeStream(this)
                        bitmap?: errorEvent.postValue(App.instance.getString(R.string.bitmap_err_msg))
                    }
                }
            }
            it.resume(bitmap)
        }
    }

    private fun requestInputStream(urlConnectionInstance: URLConnection): InputStream? {
        val inputStream: InputStream
        try {
            inputStream = urlConnectionInstance.getInputStream()
        } catch (e: IOException) {
            errorEvent.postValue(App.instance.getString(R.string.network_err_msg))
            return null
        } catch (e: UnknownServiceException) {
            errorEvent.postValue(App.instance.getString(R.string.unknown_service_err_msg))
            return null
        }
        return inputStream
    }

    private fun establishConnection(url: URL): URLConnection? {
        val urlConnectionInstance: URLConnection
        try {
            urlConnectionInstance = url.openConnection()
        } catch (e: IOException) {
            errorEvent.postValue(App.instance.getString(R.string.network_err_msg))
            return null
        }
        return urlConnectionInstance
    }

    private fun createURL(url: String): URL? {
        val urlFormed: URL
        try {
            urlFormed = URL(url)
        } catch (e: MalformedURLException) {
            errorEvent.postValue(App.instance.getString(R.string.url_err_msg))
            return null
        }
        return urlFormed
    }
    
    private fun postErrorMessage(msgID:Int){
        errorEvent.postValue(App.instance.getString(msgID))
    }

}