package com.ginzburgworks.filmfinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ginzburgworks.filmfinder.databinding.FragmentSelectionsBinding
import com.ginzburgworks.filmfinder.utils.AnimationHelper

private const val ANIM_POSITION = 1

class SelectionsFragment : Fragment() {

    private var _binding: FragmentSelectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
    }

    private fun initAnimation() {
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentSelectionsRoot,
            requireActivity(),
            ANIM_POSITION
        )
    }
}