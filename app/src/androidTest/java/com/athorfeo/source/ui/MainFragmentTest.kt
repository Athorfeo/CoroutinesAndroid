package com.athorfeo.source.ui

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.MainActivity
import com.athorfeo.source.app.ui.main.MainFragment
import com.athorfeo.source.app.ui.main.MainViewModel
import com.athorfeo.source.testing.SingleFragmentActivity
import com.athorfeo.source.util.ViewModelUtil
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private lateinit var viewModel: MainViewModel
    private val fragment = MainFragment()

    @Before
    fun init() {
        viewModel = mock(MainViewModel::class.java)
        viewModel.reset()

        fragment.viewModelFactory =  ViewModelUtil.createFor(viewModel)
        activityRule.activity.setFragment(fragment)
    }

    @Test
    fun example(){
        onView(withId(R.id.input_search)).perform(
            typeText("Batman"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )

        onView(withId(R.id.button_search)).perform(click())

        verify(viewModel).searchMovies("Batman")
    }
}