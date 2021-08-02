package com.ginzburgworks.filmfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  initButtons()
        initNavigation()


    }

    private fun initNavigation() {
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.favorites -> {
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.watch_later -> {
                    Toast.makeText(this, "Посмотреть похже", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.selections -> {
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }


    /*
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
            Toast.makeText(this, getString((R.string.btn_selections)), Toast.LENGTH_SHORT).show()
        }
        button_settings.setOnClickListener() {
            Toast.makeText(this, getString((R.string.btn_settings)), Toast.LENGTH_SHORT).show()
        }
    }
    */

}