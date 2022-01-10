package com.ginzburgworks.filmfinder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.data.local.shared.PreferenceProvider
import com.ginzburgworks.filmfinder.domain.Interactor
import javax.inject.Inject


class SettingsFragmentViewModel @Inject constructor(private val interactor: Interactor) :
    ViewModel() {

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category
    private val _nightMode = MutableLiveData<Boolean>()
    val nightMode: LiveData<Boolean> = _nightMode

    init {
        initNightMode()
        setCategory(getSavedCategory())
    }

    private fun initNightMode() {
        _nightMode.value = getSavedNightMode()
    }

    fun getSavedCategory(): String {
        return interactor.getCurrentFilmsCategory()
    }

    fun setCategory(category: String) {
        _category.value = category
        interactor.saveCurrentFilmsCategory(category)
    }

    fun onNightModeClick() {
        val currentNightMode = nightMode.value ?: true
        val nextNightMode = !currentNightMode
        val newNightModeInt = (nightModeToInt(nextNightMode))
        App.instance.setUINightMode(newNightModeInt)
        setNightMode(nextNightMode)
    }

    private fun getSavedNightMode(): Boolean {
        return nightModeToBoolean(interactor.getNightMode())
    }

    private fun setNightMode(mode: Boolean) {
        _nightMode.value = mode
        saveNightMode(mode)
    }

    private fun saveNightMode(mode: Boolean) {
        interactor.saveNightMode(nightModeToInt(mode))
    }


    private fun nightModeToBoolean(mode: Int): Boolean {
        var result = true
        if (mode == PreferenceProvider.DAY_MODE)
            result = false
        return result
    }

    private fun nightModeToInt(mode: Boolean): Int {
        var result = PreferenceProvider.DAY_MODE
        if (mode)
            result = PreferenceProvider.NIGHT_MODE
        return result
    }


}