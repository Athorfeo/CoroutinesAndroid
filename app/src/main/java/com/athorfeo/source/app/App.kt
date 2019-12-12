package com.athorfeo.source.app

import android.app.Activity
import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.athorfeo.source.BuildConfig
import com.athorfeo.source.di.AppInjector
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Singleton de aplicación de toda la APP.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class App: Application(), HasActivityInjector, CameraXConfig.Provider {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig(this)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}