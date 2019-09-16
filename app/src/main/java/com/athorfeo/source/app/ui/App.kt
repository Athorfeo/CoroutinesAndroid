package com.athorfeo.source.app.ui

import android.app.Activity
import android.app.Application
import com.athorfeo.source.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Singleton de aplicación de toda la APP.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class App: Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

    companion object {
        lateinit var instance: App
            private set
    }
}