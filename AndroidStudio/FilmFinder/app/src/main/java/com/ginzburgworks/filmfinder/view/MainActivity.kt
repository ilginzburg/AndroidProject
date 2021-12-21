package com.ginzburgworks.filmfinder.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.databinding.ActivityMainBinding
import com.ginzburgworks.filmfinder.domain.Film
import com.ginzburgworks.filmfinder.view.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        tag = HOME_FRAGMENT_TAG
        if (nightModeSwitched)
            tag = SETTINGS_FRAGMENT_TAG
        val fragment = checkFragmentExistence(tag)
        changeFragment(fragment ?: HomeFragment(), tag)
    }

    private fun initNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val fragment = checkFragmentExistence(HOME_FRAGMENT_TAG)
                    changeFragment(fragment ?: HomeFragment(), HOME_FRAGMENT_TAG)
                    true
                }
                R.id.favorites -> {
                    val fragment = checkFragmentExistence(FAVORITES_FRAGMENT_TAG)
                    changeFragment(fragment ?: FavoritesFragment(), FAVORITES_FRAGMENT_TAG)
                    true
                }
                R.id.watch_later -> {
                    val fragment = checkFragmentExistence(WATCH_LATER_FRAGMENT_TAG)
                    changeFragment(fragment ?: WatchLaterFragment(), WATCH_LATER_FRAGMENT_TAG)
                    true
                }
                R.id.selections -> {
                    val fragment = checkFragmentExistence(SELECTIONS_FRAGMENT_TAG)
                    changeFragment(fragment ?: SelectionsFragment(), SELECTIONS_FRAGMENT_TAG)
                    true
                }
                R.id.settings -> {
                    val fragment = checkFragmentExistence(SETTINGS_FRAGMENT_TAG)
                    changeFragment(fragment ?: SettingsFragment(), SETTINGS_FRAGMENT_TAG)
                    true
                }
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    fun launchDetailsFragment(film: Film) {
        val bundle = Bundle()
        bundle.putParcelable(DetailsFragment.KEY_FILM, film)
        val fragment = DetailsFragment()
        fragment.arguments = bundle
        changeFragment(fragment, DETAILS_FRAGMENT_TAG)
    }

    companion object{
        private const val DETAILS_FRAGMENT_TAG = "details"
        private const val HOME_FRAGMENT_TAG = "home"
        private const val SETTINGS_FRAGMENT_TAG = "settings"
        private const val SELECTIONS_FRAGMENT_TAG = "selections"
        private const val WATCH_LATER_FRAGMENT_TAG = "watch_later"
        private const val FAVORITES_FRAGMENT_TAG = "favorites"
    }
}