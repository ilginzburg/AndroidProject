package com.ginzburgworks.filmfinder.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.view.MainActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

private const val ANIM_DURATION = 3500L
private const val ANIM_ALFA_START = 0f
private const val ANIM_ALFA_END = 1f

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val animatedVectorDrawable =
            AnimatedVectorDrawableCompat.create(this, R.drawable.anim_vector)
        animated_view.setImageDrawable(animatedVectorDrawable)
        animated_view.alpha = ANIM_ALFA_START
        animated_view.animate().setDuration(ANIM_DURATION).alpha(ANIM_ALFA_END).withEndAction {
            animatedVectorDrawable?.start()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}