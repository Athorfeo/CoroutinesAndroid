package com.athorfeo.source.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.athorfeo.source.app.ui.main.MainViewModel
import com.athorfeo.source.app.viewmodel.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(maineViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}