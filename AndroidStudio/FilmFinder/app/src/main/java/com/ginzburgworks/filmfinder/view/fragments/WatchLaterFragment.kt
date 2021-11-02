package com.ginzburgworks.filmfinder.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.utils.AnimationHelper
import kotlinx.android.synthetic.main.fragment_selected.*
import kotlinx.android.synthetic.main.fragment_watch_later.*

private const val ANIM_POSITION = 4

class WatchLaterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_watch_later, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(watch_later_fragment_root, requireActivity(), ANIM_POSITION)
    }

}