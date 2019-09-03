package com.athorfeo.source.di.module

import com.athorfeo.source.app.ui.MainActivity
import com.athorfeo.source.app.ui.base.activity.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
    abstract fun contributeBaseActivity(): BaseActivity
}