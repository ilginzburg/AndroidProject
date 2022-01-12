package com.ginzburgworks.filmfinder.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.domain.Interactor
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.NEXT_PAGE
import com.ginzburgworks.filmfinder.domain.SingleLiveEvent
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val MAX_TIME_AFTER_BD_UPDATE = 600000

class HomeFragmentViewModel @Inject constructor(private val interactor: Interactor) : ViewModel() {

    @Inject
    lateinit var adapter: FilmListRecyclerAdapter
    val itemsForSearch = mutableListOf<Film>()
    lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    var totalNumberOfPages = interactor.getTotalPagesNumber()
    val showProgressBar = Channel<Boolean>(Channel.CONFLATED)
    val pageOfFilmsFromRemoteDataSourceToUI = Channel<List<Film>>(Channel.CONFLATED)
    val pageOfFilmsFromLocalDataSourceToUI = Channel<List<Film>>(Channel.CONFLATED)
    val errorEvent = SingleLiveEvent<String>()

    private val handler =
        CoroutineExceptionHandler { _, e -> errorEvent.postValue(App.instance.getString(R.string.exc_handler_msg) + e) }


    private fun clearLocalDataSource() {
        viewModelScope.launch(handler) {
            launch {
                interactor.clearLocalDataSource()
            }
        }
    }

    fun isLocalDataSourceNeedToUpdate(): Boolean {
        if (isLastUpdateEarlierThanPredefinedMaxTime(interactor.getLocalDataSourceUpdateTime())) {
            clearLocalDataSource()
            return true
        }
        return false
    }

    fun requestNextPageFromRemote() {
        viewModelScope.launch(handler) {
            launch(Dispatchers.IO) {
                showProgressBar.send(true)
                interactor.getPageOfFilmsFromRemoteDataSource(NEXT_PAGE)
                interactor.pageFromRemote.let {
                    for(element in it) {
                        pageOfFilmsFromRemoteDataSourceToUI.send(element)
                        break
                    }
                }
                showProgressBar.send(false)
            }
        }
    }

    fun requestNextPageFromLocal() {
        viewModelScope.launch(handler) {
            launch(Dispatchers.IO) {
                showProgressBar.send(true)
                interactor.getPageOfFilmsFromLocalDataSourceToUI(NEXT_PAGE)
                interactor.pageFromLocal.let {
                    for(element in it) {
                        pageOfFilmsFromLocalDataSourceToUI.send(element)
                        break
                    }
                }
                showProgressBar.send(false)
            }
        }
    }

    private fun isLastUpdateEarlierThanPredefinedMaxTime(updateTimeInMs: Long): Boolean {
        val currentTimeInMs = Calendar.getInstance().timeInMillis
        return (currentTimeInMs - updateTimeInMs) > MAX_TIME_AFTER_BD_UPDATE
    }

    fun registerOnChangeListener() {
        interactor.registerPreferencesListener(onSharedPreferenceChangeListener)
    }

    override fun onCleared() {
        super.onCleared()
        if (this::onSharedPreferenceChangeListener.isInitialized)
            interactor.unRegisterPreferencesListener(onSharedPreferenceChangeListener)
    }

}
