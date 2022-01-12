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
import com.ginzburgworks.filmfinder.databinding.FragmentSettingsBinding
import com.ginzburgworks.filmfinder.viewmodels.SettingsFragmentViewModel
import javax.inject.Inject
import javax.inject.Singleton


open class SettingsFragment : Fragment() {

    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding
    private lateinit var appContext: Context
    private val fragmentSettingsViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[SettingsFragmentViewModel::class.java]
    }

    @Singleton
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


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

        fragmentSettingsBinding.apply {
            this.fragmentViewModel = fragmentSettingsViewModel
        }
        App.instance.nightModeSwitched = true
    }
}