package com.athorfeo.source.di.module

import androidx.fragment.app.DialogFragment
import com.athorfeo.source.app.ui.cameraOld.CameraOldFragment
import com.athorfeo.source.app.ui.dashboard.DashboardFragment
import com.athorfeo.source.app.ui.cameraX.CameraXFragment
import com.athorfeo.source.app.ui.custom.CustomFragment
import com.athorfeo.source.app.ui.dialogs.DialogsFragment
import com.athorfeo.source.app.ui.settings.SettingsFragment
import com.athorfeo.source.app.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun dashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun customFragment(): CustomFragment

    @ContributesAndroidInjector
    abstract fun cameraXFragment(): CameraXFragment

    @ContributesAndroidInjector
    abstract fun dialogFragment(): DialogsFragment

    @ContributesAndroidInjector
    abstract fun cameraOldFragment(): CameraOldFragment


}