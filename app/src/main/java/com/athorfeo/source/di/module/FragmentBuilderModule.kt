package com.athorfeo.source.di.module

import com.athorfeo.source.app.ui.custom.CustomFragment
import com.athorfeo.source.app.ui.settings.SettingsFragment
import com.athorfeo.source.app.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeCustomFragment(): CustomFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment
}