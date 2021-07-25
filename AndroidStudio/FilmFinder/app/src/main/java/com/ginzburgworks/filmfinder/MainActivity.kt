package com.ginzburgworks.filmfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }

    private fun initButtons() {
        button_menu.setOnClickListener() {
            Toast.makeText(this, getString((R.string.btn_menu)), Toast.LENGTH_SHORT).show()
        }
        button_favorite.setOnClickListener() {
            Toast.makeText(this, getString((R.string.btn_favorite)), Toast.LENGTH_SHORT).show()
        }
        button_watch_later.setOnClickListener() {
            Toast.makeText(this, getString((R.string.btn_watch_later)), Toast.LENGTH_SHORT).show()
        }
        button_properties.setOnClickListener() {
            Toast.makeText(this, getString((R.string.btn_properties)), Toast.LENGTH_SHORT).show()
        }
        button_settings.setOnClickListener() {
            Toast.makeText(this, getString((R.string.btn_settings)), Toast.LENGTH_SHORT).show()
        }
    }
}