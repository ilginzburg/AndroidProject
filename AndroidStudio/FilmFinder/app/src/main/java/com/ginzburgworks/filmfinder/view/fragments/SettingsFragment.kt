package com.ginzburgworks.filmfinder.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.databinding.FragmentSettingsBinding
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import com.ginzburgworks.filmfinder.viewmodels.SettingsFragmentViewModel
import javax.inject.Inject
import javax.inject.Singleton


open class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var appContext: Context

    private val viewModel by activityViewModels<SettingsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.apply {
            this?.viewModel = viewModel
        }
        App.instance.nightModeSwitched = true
    }

}