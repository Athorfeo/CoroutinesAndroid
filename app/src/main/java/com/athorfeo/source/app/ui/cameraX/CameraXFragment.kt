package com.athorfeo.source.app.ui.cameraX

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.base.fragment.BaseFragment
import com.athorfeo.source.databinding.FragmentCameraxBinding
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ui.BarcodeDialogFragment
import com.google.zxing.Result

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class CameraXFragment: BaseFragment(), View.OnClickListener, BarcodeDialogFragment.OnBarcodeListener{
    private lateinit var binding: FragmentCameraxBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camerax, container, false)
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
                fragmentManager?.let {
                    BarcodeDialogFragment
                        .newInstance(this)
                        .show(it, "DialogBarcode")
                }
            }
        }
    }

    @MainThread
    override fun onBarcodeSuccess(result: Result) {
        binding.lastBarcode = result.text
    }

    private fun init(){
        binding.clickListener = this
    }
}