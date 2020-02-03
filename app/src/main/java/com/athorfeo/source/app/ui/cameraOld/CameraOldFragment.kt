package com.athorfeo.source.app.ui.cameraOld

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.athorfeo.source.R
import com.athorfeo.source.app.ui.base.fragment.BaseFragment
import com.athorfeo.source.databinding.FragmentCameraOldBinding
import com.athorfeo.source.databinding.FragmentCameraxBinding
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.ui.BarcodeDialogFragment
import com.google.zxing.Result
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
class CameraOldFragment: BaseFragment(), View.OnClickListener{
    private lateinit var binding: FragmentCameraOldBinding
    lateinit var currentPhotoPath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera_old, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonOpenDialog.text = "Take photo"
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if( requestCode == 1 ){
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            binding.imageCamera.setImageBitmap(bitmap)

            binding.imageCameraRotate.setImageBitmap(fixImage(bitmap, currentPhotoPath))
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_open_dialog -> {
                if(allPermissionsGranted()){
                    dispatchTakePictureIntent()
                }else{
                    ActivityCompat.requestPermissions(
                        activity!!,
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            }
        }
    }

    private fun init(){
        binding.clickListener = this
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(activity?.baseContext!!, it) == PackageManager.PERMISSION_GRANTED
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val privateFolder = validateFolder(activity?.applicationContext!!)
        privateFolder.second?.let {
            return File.createTempFile(
                "JPEG_PROFILE_", /* prefix */
                ".jpg", /* suffix */
                it/* directory */
            ).apply {
                Timber.i(path)
                currentPhotoPath = absolutePath
            }
        }
        return null
    }

    private fun dispatchTakePictureIntent() {
        context?.let {context ->
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            context,
                            "com.athorfeo.source.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
        }
    }

    private fun fixImage(bitmap: Bitmap, path: String): Bitmap{
        val ei = ExifInterface(path)

        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Float): Bitmap{
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun validateFolder(context: Context): Pair<Boolean?, File?> {
        val contextWrapper = ContextWrapper(context)
        val directory = contextWrapper.getDir("vipay_folder", Context.MODE_PRIVATE)
        if(!directory.exists()){
            val createFolder = directory.mkdirs()
            if(!createFolder){
                return Pair(false, directory)
            }
        }
        return Pair(true, directory)
    }

    companion object{
        const val REQUEST_TAKE_PHOTO = 1
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}