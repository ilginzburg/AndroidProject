package com.ginzburgworks.filmfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //Получаем наш фильм из переданного бандла
        val film: Film? = intent.extras?.get("film") as Film
        details_toolbar.title = film?.title?:"none"
        details_poster.setImageResource(film?.poster?:(R.drawable.corner_background))
        details_description.text = film?.description?:"none"
    }
}