package com.ginzburgworks.filmfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val animatedVectorDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.anim_vector)
        animated_view.setImageDrawable(animatedVectorDrawable)
        animated_view.alpha = 0f
        animated_view.animate().setDuration(3500).alpha(1F).withEndAction {
            animatedVectorDrawable?.start()
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}