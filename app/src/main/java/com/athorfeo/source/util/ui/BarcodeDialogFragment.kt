package com.athorfeo.source.util.ui

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.AspectRatio
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.cameraX.QrCodeAnalyzer
import com.athorfeo.source.databinding.DialogBarcodeBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.Result
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class BarcodeDialogFragment(
    private val listener: OnBarcodeListener
): DialogFragment(), View.OnClickListener{
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var binding: DialogBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_rounded)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_barcode, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickListener = this
        cameraProviderFuture = ProcessCameraProvider.getInstance(context!!)

        try {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }catch (exception: Exception){

        }
    }

    override fun onPause() {
        super.onPause()
        closeDialog()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_negative -> {
                closeDialog()
            }
        }
    }

    private fun closeDialog(){
        //cameraProviderFuture.get().unbindAll()
        dismiss()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(activity?.baseContext!!, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera(){
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        val preview : Preview = Preview.Builder()
            .setTargetName("Preview")
            .build()

        preview.previewSurfaceProvider = binding.barcodePreview.previewSurfaceProvider

        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(LensFacing.BACK)
            .build()

        val imageAnalysis = ImageAnalysis
            .Builder()
            .setBackpressureStrategy(ImageAnalysis.BackpressureStrategy.KEEP_ONLY_LATEST)
            .build().also {
                it.setAnalyzer(executor, QrCodeAnalyzer{result ->
                    closeDialog()
                    listener.onBarcodeSuccess(result)
                })
            }

        cameraProvider.bindToLifecycle(
            viewLifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview)
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        @JvmStatic
        fun newInstance(listener: OnBarcodeListener) =
            BarcodeDialogFragment(listener).apply {
                arguments = Bundle().apply {
                }
            }
    }

    interface OnBarcodeListener{
        fun onBarcodeSuccess(result: Result)
    }
}