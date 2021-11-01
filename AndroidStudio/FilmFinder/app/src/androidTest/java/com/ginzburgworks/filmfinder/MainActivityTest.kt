package com.ginzburgworks.filmfinder

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.ginzburgworks.filmfinder.view.MainActivity
import com.ginzburgworks.filmfinder.view.rv_viewholders.FilmViewHolder
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun recyclerViewShouldBeAttached() {
        onView(withId(R.id.main_recycler)).check(matches(isDisplayed()))
        onView(withId(R.id.main_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition<FilmViewHolder>(0, click()))
    }

    @Test
    fun searchViewShouldBeAbleToInputText() {
        val testString = "1111111"
        onView(withId(R.id.search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.search_view)).perform(typeSearchViewText(testString))
    }

    private fun typeSearchViewText(text: String?): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun getDescription(): String {
                return "Change view text"
            }

            override fun perform(uiController: UiController?, view: View) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }

    @Test
    fun allMenuDestinationsShouldWork() {
        onView(withId(R.id.favorites)).perform(click())
        onView(withId(R.id.favorites_fragment_root)).check(matches(isDisplayed()))

        onView(withId(R.id.watch_later)).perform(click())
        onView(withId(R.id.watch_later_fragment_root)).check(matches(isDisplayed()))

        onView(withId(R.id.selections)).perform(click())
        onView(withId(R.id.selected_fragment_root)).check(matches(isDisplayed()))

        onView(withId(R.id.home)).perform(click())
        onView(withId(R.id.home_fragment_root)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldOpenDetailsFragment() {
        onView(withId(R.id.main_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition<FilmViewHolder>(0, click()))
        onView(withId(R.id.app_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun addToFavoritesButtonClickable() {
        onView(withId(R.id.main_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition<FilmViewHolder>(0, click()))
        onView(withId(R.id.details_fab_favorites)).perform(click())
        onView(withId(R.id.details_fab_favorites)).perform(click())
    }

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    @Test
    fun openNotifications() {
        device.openNotification()
        //Немного притормозим выполнение кода, чтобы шторка успела открыться
        Thread.sleep(1000)
        device.click(100, 1200)
    }

    @Test
    fun changeOrientation() {
        onView(withId(R.id.main_recycler))
        device.unfreezeRotation();
        device.setOrientationLeft();
    }
}