package com.ginzburgworks.filmfinder.utils

import android.app.Activity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.concurrent.Executors
import kotlin.math.hypot
import kotlin.math.roundToInt

private const val START_RADIUS = 0f
private const val DURATION = 500L
private const val MENU_ITEMS = 5

object AnimationHelper {
    fun performFragmentCircularRevealAnimation(rootView: View, activity: Activity, position: Int) {
        Executors.newSingleThreadExecutor().execute {
            while (true) {
                if (rootView.isAttachedToWindow) {
                    activity.runOnUiThread {
                        val itemCenter = rootView.width / (MENU_ITEMS * 2)
                        val step = (itemCenter * 2) * (position - 1) + itemCenter
                        val x: Int = step
                        val y: Int = rootView.y.roundToInt() + rootView.height
                        val endRadius = hypot(rootView.width.toDouble(), rootView.height.toDouble())
                        ViewAnimationUtils.createCircularReveal(rootView, x, y, START_RADIUS, endRadius.toFloat()).apply {
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