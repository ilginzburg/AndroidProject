package com.ginzburgworks.filmfinder

import android.app.Activity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.concurrent.Executors
import kotlin.math.hypot
import kotlin.math.roundToInt

const val startRadius = 0
const val DURATION = 500L
private const val menuItems = 4

object AnimationHelper {
    fun performFragmentCircularRevealAnimation(rootView: View, activity: Activity, position: Int) {
        Executors.newSingleThreadExecutor().execute {
            while (true) {
                if (rootView.isAttachedToWindow) {
                    activity.runOnUiThread {
                        val itemCenter = rootView.width / (menuItems * 2)
                        val step = (itemCenter * 2) * (position - 1) + itemCenter
                        val x: Int = step
                        val y: Int = rootView.y.roundToInt() + rootView.height
                        val endRadius = hypot(rootView.width.toDouble(), rootView.height.toDouble())
                        ViewAnimationUtils.createCircularReveal(rootView, x, y, startRadius.toFloat(), endRadius.toFloat()).apply {
                            duration = DURATION
                            interpolator = AccelerateDecelerateInterpolator()
                            start()
                        }
                        rootView.visibility = View.VISIBLE
                    }
                    return@execute
                }
            }
        }
    }
}