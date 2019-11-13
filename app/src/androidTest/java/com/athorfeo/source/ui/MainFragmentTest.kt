package com.athorfeo.source.ui

import android.view.KeyEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.athorfeo.source.R
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.app.ui.MainActivity
import com.athorfeo.source.app.ui.main.MainFragment
import com.athorfeo.source.app.ui.main.MainViewModel
import com.athorfeo.source.testing.SingleFragmentActivity
import com.athorfeo.source.util.SingleLiveEvent
import com.athorfeo.source.util.ViewModelUtil
import com.athorfeo.source.util.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private lateinit var viewModel: MainViewModel
    private val isLoading = SingleLiveEvent<Boolean>()
    private val isError = SingleLiveEvent<ErrorResource>()
    private val _movies = MediatorLiveData<List<Movie>>()
    private val movies: LiveData<List<Movie>> = _movies
    private val fragment = TestMainFragment()

    @Before
    fun init() {
        viewModel = mock(MainViewModel::class.java)
        `when`(viewModel.isLoading).thenReturn(isLoading)
        `when`(viewModel.isError).thenReturn(isError)
        `when`(viewModel._movies).thenReturn(_movies)
        `when`(viewModel.movies).thenReturn(movies)

        fragment.viewModelFactory =  ViewModelUtil.createFor(viewModel)
        activityRule.activity.setFragment(fragment)
    }

    @Test
    fun searchTest(){
        onView(withId(R.id.input_search)).perform(
            typeText("Batman"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )

        onView(withId(R.id.button_search)).perform(click())

        verify(viewModel).searchMovies("Batman")
    }

    @Test
    fun resetTest(){
        onView(withId(R.id.button_reset)).perform(click())
        verify(viewModel).reset()
    }

    @Test
    fun filterTest(){
        onView(withId(R.id.button_filter)).perform(click())
        verify(viewModel).filter(listOf())
    }

    class TestMainFragment : MainFragment() {
        val navController = mock<NavController>()
        override fun navController() = navController
    }
}