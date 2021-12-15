package com.ginzburgworks.filmfinder.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.domain.Interactor
import javax.inject.Inject

class SettingsFragmentViewModel @Inject constructor(private val interactor: Interactor) : ViewModel() {

    val categoryPropertyLifeData: MutableLiveData<String> = MutableLiveData()

    init {
        getCategoryProperty()
    }

    private fun getCategoryProperty() {
        categoryPropertyLifeData.value = interactor.getDefaultCategoryFromPreferences()
    }

    fun putCategoryProperty(category: String) {
        interactor.saveDefaultCategoryToPreferences(category)
        getCategoryProperty()
    }
}