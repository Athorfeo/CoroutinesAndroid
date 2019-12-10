package com.athorfeo.source.app.ui.cameraX
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.base.fragment.BaseFragment
import com.athorfeo.source.databinding.FragmentCameraxBinding
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ui.BarcodeDialogFragment
import com.google.common.util.concurrent.ListenableFuture
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class CameraXFragment: BaseFragment(), View.OnClickListener{
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
                    BarcodeDialogFragment.newInstance().show(it, "DialogBarcode")
                }
            }
        }
    }

    private fun init(){
        binding.clickListener = this
    }
}