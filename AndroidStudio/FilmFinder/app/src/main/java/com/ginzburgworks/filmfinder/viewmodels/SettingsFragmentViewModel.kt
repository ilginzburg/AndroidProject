package com.ginzburgworks.filmfinder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.domain.Interactor
import javax.inject.Inject


class SettingsFragmentViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category
    private val _nightMode = MutableLiveData<Boolean>()
    val nightMode: LiveData<Boolean> = _nightMode

    init {
        App.instance.appComponent.injectSettingsVM(this)
        setCategory(getSavedCategory())
    }

    fun initNightMode() {
        _nightMode.value = getSavedNightMode()
    }

    private fun getSavedCategory(): String {
        return interactor.getCurrentFilmsCategory()
    }

    fun setCategory(category: String) {
        _category.value = category
        interactor.saveCurrentFilmsCategory(category)
    }

    fun onNightModeClick() {
        val currentNightMode = nightMode.value ?: true
        setNightMode(!currentNightMode)
    }

    private fun setNightMode(newNightMode: Boolean) {
        val newNightModeInt = (interactor.nightModeToInt(newNightMode))
        App.instance.setUINightMode(newNightModeInt)
        App.instance.nightModeIsOnBecauseOfLowBattery = false
        _nightMode.value = newNightMode
    }

    private fun getSavedNightMode(): Boolean {
        return interactor.getNightModeBool()
    }

}