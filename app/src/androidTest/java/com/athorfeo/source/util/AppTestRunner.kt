package com.athorfeo.source.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.athorfeo.source.TestApp

class AppTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}