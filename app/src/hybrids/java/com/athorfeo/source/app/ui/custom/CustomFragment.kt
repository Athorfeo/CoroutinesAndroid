package com.athorfeo.source.app.ui.custom

import android.app.FragmentManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.athorfeo.source.BuildConfig
import com.athorfeo.source.R
import com.athorfeo.source.api.response.SearchMoviesResponse
import com.athorfeo.source.app.ui.base.fragment.BaseFragment
import com.athorfeo.source.databinding.FragmentCustomBinding
import com.athorfeo.source.databinding.FragmentMainBinding
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ResponseCode
import com.athorfeo.source.util.findFieldAnnotation
import com.athorfeo.source.util.ui.CustomDialogFragment
import com.athorfeo.source.util.ui.DialogUtil
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.annotations.TestOnly
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class CustomFragment: BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCustomBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        init()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_open_dialog -> {
                CustomDialogFragment().show(fragmentManager!!, "")
            }
        }
    }

    private fun init(){
        binding.clickListener = this
        val text = "${BuildConfig.FLAVOR} - ${BuildConfig.BUILD_TYPE}"
        binding.textTypeBuild.text = text
    }
}