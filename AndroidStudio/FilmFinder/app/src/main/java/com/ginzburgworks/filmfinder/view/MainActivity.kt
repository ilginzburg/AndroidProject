package com.ginzburgworks.filmfinder.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.databinding.ActivityMainBinding
import com.ginzburgworks.filmfinder.view.fragments.*
import com.ginzburgworks.local_module.Film

private const val DETAILS_FRAGMENT_TAG = "details"
private const val HOME_FRAGMENT_TAG = "home"
private const val SETTINGS_FRAGMENT_TAG = "settings"
private const val SELECTIONS_FRAGMENT_TAG = "selections"
private const val WATCH_LATER_FRAGMENT_TAG = "watch_later"
private const val FAVORITES_FRAGMENT_TAG = "favorites"
private const val BATTERY_LOW_MSG = "Батарея скоро разрядится"
private const val POWER_CONNECTED_MSG = "Подключено зарядное устройство"


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var fragmentTag: String
    private val receiver = ChargeEventsReceiver()
    private val filter = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        initNavigation()
        setInitialFragment()
        initChargeEventsReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun initChargeEventsReceiver() {
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_BATTERY_LOW)
        registerReceiver(receiver, filter)
    }

    inner class ChargeEventsReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_BATTERY_LOW -> {
                    Toast.makeText(
                        context, BATTERY_LOW_MSG, Toast.LENGTH_SHORT
                    ).show()
                    App.instance.switchToNightMode()
                }

                Intent.ACTION_POWER_CONNECTED -> {
                    Toast.makeText(
                        context, POWER_CONNECTED_MSG, Toast.LENGTH_SHORT
                    ).show()
                    if (App.instance.nightModeIsOnBecauseOfLowBattery) App.instance.switchToDayMode()
                }
            }
        }
    }

    private fun setInitialFragment() {
        fragmentTag = HOME_FRAGMENT_TAG
        if (App.instance.nightModeSwitched) fragmentTag = SETTINGS_FRAGMENT_TAG
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
        supportFragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null).commit()
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