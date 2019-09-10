package com.athorfeo.source.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.athorfeo.source.app.ui.base.activity.BaseActivityViewModel
import com.athorfeo.source.app.ui.main.MainViewModel
import com.athorfeo.source.app.viewmodel.AppViewModelFactory
import com.athorfeo.source.di.key.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(BaseActivityViewModel::class)
    abstract fun bindBaseActivityViewModel(baseActivityViewModel: BaseActivityViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}