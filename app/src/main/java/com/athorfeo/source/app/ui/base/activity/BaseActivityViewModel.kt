package com.athorfeo.source.app.ui.base.activity

import com.athorfeo.source.app.viewmodel.BaseViewModel
import com.athorfeo.source.util.SingleLiveEvent
import javax.inject.Inject

/**
 * Manejo de las funcionalidades de las actividades base
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class BaseActivityViewModel @Inject constructor(): BaseViewModel(){
    val language = SingleLiveEvent<String>()
}