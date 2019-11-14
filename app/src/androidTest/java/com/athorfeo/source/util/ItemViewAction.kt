package com.athorfeo.source.util

import android.view.View
import android.widget.Button
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

fun clickChild(id: Int): ViewAction {
    return object : ViewAction {

        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }
}