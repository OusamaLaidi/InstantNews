package com.instant.news.ui

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.instant.news.R
import com.instant.news.ui.NewsActivityTest.CustomAssertions.Companion.hasItemCount
import com.instant.news.utils.API_BASE_URL
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test


class NewsActivityTest{

    private val mockWebServer = MockWebServer()

    private lateinit var scenario: ActivityScenario<NewsActivity>

    private val responseBody = "{\"status\":\"ok\",\"totalResults\":1,\"articles\":[{\"source\":{\"id\":null,\"name\":\"20 Minutes\"},\"author\":\"Fabien Randanne\",\"title\":\"« Pékin Express » : « On se voyait en finale avec les frères belges », confient Tarik et Ahmed - 20 Minutes\",\"description\":\"Les « cousins farceurs » du jeu de M6 ont quitté l’aventure à l’issue de la demi-finale diffusée jeudi\",\"url\":\"https://www.20minutes.fr/arts-stars/television/3266123-20220407-pekin-express-voyait-finale-freres-belges-confient-tarik-ahmed\",\"urlToImage\":\"https://img.20mn.fr/RGVg_k0BR1GoMnxmySK7WSk/1200x768_tarik-ahmed-cousins-farceurs-pekin-express\",\"publishedAt\":\"2022-04-07T21:20:49Z\",\"content\":\"La chance était du côté de Jérémy et Fanny. La demi-finale de Pékin Express, sur les terres de l’aigle royal diffusée ce jeudi sur  M6 s’est achevée par l’élimination d’Ahmed et Tarik. Les « cousins … [+3582 chars]\"}]}"

    @Before
    fun setup(){
        /**
         * the test must be executed with mock data, set API_BASE_URL= "http://127.0.0.1:8080"
         */
        assert(API_BASE_URL == "http://127.0.0.1:8080")

        mockWebServer.start(8080)

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)



    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun check_layout_when_loading() {
        onView(withId(R.id.activityIndicator))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.errorLoadView))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun check_layout_after_loading_when_article_count_is_1() {
        setResponseCode(200)

        onView(withId(R.id.activityIndicator))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.errorLoadView))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.recyclerView))
            .check(hasItemCount(1))
    }

    @Test
    fun check_layout_after_error_loading() {
        setResponseCode(500)

        onView(withId(R.id.activityIndicator))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.errorLoadView))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.recyclerView))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.recyclerView))
            .check(hasItemCount(0))
    }

    @Test
    fun check_if_article_detail_is_open_after_item_click() {
        setResponseCode(200)

        onView(withId(R.id.recyclerView))
            .check(hasItemCount(1))
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(500)
        onView(withId(R.id.layout_activity_article_detail))
            .check(matches(isDisplayed()))
    }

    private fun setResponseCode(responseCode:Int){
        val mockResponse = MockResponse()
            .setResponseCode(responseCode)
            .setBody(responseBody)
        mockWebServer.enqueue(mockResponse)
        Thread.sleep(300)
    }

    class CustomAssertions {
        companion object {
            fun hasItemCount(count: Int): ViewAssertion {
                return RecyclerViewItemCountAssertion(count)
            }
        }

        private class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

            override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
                if (noViewFoundException != null) {
                    throw noViewFoundException
                }
                if (view !is RecyclerView) {
                    throw IllegalStateException("The asserted view is not RecyclerView")
                }

                if (view.adapter == null) {
                    throw IllegalStateException("No adapter is assigned to RecyclerView")
                }

                assertThat("RecyclerView item count", view.adapter!!.itemCount, CoreMatchers.equalTo(count))
            }
        }
    }
}