package com.ginzburgworks.filmfinder.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.databinding.ActivityMainBinding
import com.ginzburgworks.filmfinder.view.fragments.*


private const val DETAILS_FRAGMENT_TAG = "details"
private const val HOME_FRAGMENT_TAG = "home"
private const val SETTINGS_FRAGMENT_TAG = "settings"
private const val SELECTIONS_FRAGMENT_TAG = "selections"
private const val WATCH_LATER_FRAGMENT_TAG = "watch_later"
private const val FAVORITES_FRAGMENT_TAG = "favorites"


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var fragmentTag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        initNavigation()
        setInitialFragment()

    }

    private fun setInitialFragment() {
        fragmentTag = HOME_FRAGMENT_TAG
        if (App.instance.nightModeSwitched)
            fragmentTag = SETTINGS_FRAGMENT_TAG
        val fragment = checkFragmentExistence(fragmentTag)
        changeFragment(fragment ?: HomeFragment(), fragmentTag)
    }

    private fun initNavigation() {
        activityMainBinding.bottomNavigation.setOnItemSelectedListener {
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
        bundle.putParcelable(KEY_FILM, film)
        val fragment = DetailsFragment()
        fragment.arguments = bundle
        changeFragment(fragment, DETAILS_FRAGMENT_TAG)
    }

}