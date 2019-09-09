package com.athorfeo.source.app.ui.base.activity

import com.athorfeo.source.app.viewmodel.BaseViewModel
import com.athorfeo.source.utility.SingleLiveEvent
import javax.inject.Inject

class BaseActivityViewModel @Inject constructor(): BaseViewModel(){
    val language = SingleLiveEvent<String>()
}