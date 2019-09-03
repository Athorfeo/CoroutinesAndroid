package com.athorfeo.source.app.ui.base.activity

import androidx.lifecycle.*
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.repository.MovieRepository
import com.athorfeo.source.app.viewmodel.BaseViewModel
import com.athorfeo.source.utility.SingleLiveEvent
import com.athorfeo.source.utility.constant.ErrorCode
import com.athorfeo.source.utility.ui.DialogUtil
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BaseActivityViewModel @Inject constructor(): BaseViewModel(){
    val language = SingleLiveEvent<String>()
}