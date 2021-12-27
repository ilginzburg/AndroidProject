package com.ginzburgworks.filmfinder.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ginzburgworks.filmfinder.domain.Interactor
import javax.inject.Inject

private const val SAVE_DEFAULT_VALUE_FOR_OFFLINE = 0

class SettingsFragmentViewModel @Inject constructor(private val interactor: Interactor) :
    ViewModel() {

    val categoryPropertyLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        getCategoryProperty()
    }

    private fun getCategoryProperty() {
        categoryPropertyLiveData.value = interactor.getFilmsCategoryFromPreferences()
    }

    fun putCategoryProperty(category: String) {
        interactor.saveFilmsCategoryToPreferences(category)
        interactor.saveTotalPagesNumberToPreferences(SAVE_DEFAULT_VALUE_FOR_OFFLINE, category)
        getCategoryProperty()
    }
}