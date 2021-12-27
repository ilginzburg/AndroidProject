package com.ginzburgworks.filmfinder.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.SettingsManager
import com.ginzburgworks.filmfinder.databinding.FragmentSettingsBinding
import com.ginzburgworks.filmfinder.viewmodels.SettingsFragmentViewModel
import javax.inject.Inject
import javax.inject.Singleton

private const val ANIM_POSITION = 5


var nightModeSwitched = false

open class SettingsFragment : Fragment() {

    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding
    private lateinit var appContext: Context
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[SettingsFragmentViewModel::class.java]
    }

    @Singleton
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.instance.appComponent.inject(this)
        appContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return fragmentSettingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initNightMode()
        subscribeToCategoryPropertyChanges()
        initCategoryOnCheckedChangeListener()
    }

    private fun subscribeToCategoryPropertyChanges() {
        viewModel.categoryPropertyLiveData.observe(viewLifecycleOwner, {
            when (it) {
                POPULAR_CATEGORY -> checkCategoryRadioButton(R.id.radio_popular)
                TOP_RATED_CATEGORY -> checkCategoryRadioButton(R.id.radio_top_rated)
                UPCOMING_CATEGORY -> checkCategoryRadioButton(R.id.radio_upcoming)
                NOW_PLAYING_CATEGORY -> checkCategoryRadioButton(R.id.radio_now_playing)
            }
        })
    }

    private fun checkCategoryRadioButton(buttonID: Int) {
        fragmentSettingsBinding.radioGroup.check(buttonID)
    }

    private fun initCategoryOnCheckedChangeListener() {
        fragmentSettingsBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_popular -> putActualCategoryPropertyAfterChange(POPULAR_CATEGORY)
                R.id.radio_top_rated -> putActualCategoryPropertyAfterChange(TOP_RATED_CATEGORY)
                R.id.radio_upcoming -> putActualCategoryPropertyAfterChange(UPCOMING_CATEGORY)
                R.id.radio_now_playing -> putActualCategoryPropertyAfterChange(NOW_PLAYING_CATEGORY)
            }
        }
    }

    private fun putActualCategoryPropertyAfterChange(category: String) {
        viewModel.putCategoryProperty(category)
    }

    private fun initNightMode() {
        updateNightModeCheckBox()
        initNightModeOnCheckedChangeListener()
    }

    private fun initNightModeOnCheckedChangeListener() {
        fragmentSettingsBinding.nightMode.setOnCheckedChangeListener { _, isChecked ->
            nightModeSwitched = true
            settingsManager.setSelectedNightMode(isChecked)
        }
    }

    private fun updateNightModeCheckBox() {
        fragmentSettingsBinding.nightMode.isChecked = settingsManager.savedNightMode
    }

    companion object {
        const val POPULAR_CATEGORY = "popular"
        const val TOP_RATED_CATEGORY = "top_rated"
        const val UPCOMING_CATEGORY = "upcoming"
        const val NOW_PLAYING_CATEGORY = "now_playing"
    }
}