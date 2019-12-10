package com.athorfeo.source.app.ui.cameraX

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.Result

/* https://stackoverflow.com/questions/58113159/how-to-use-zxing-wih-android-camerax-to-decode-barcode-and-qr-codes */
class ZxingQrCodeAnalyzer(
    private val onQrCodesDetected: (qrCode: Result) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        val reader = MultiFormatReader()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy, rotationDegrees: Int) {
        try {
            imageProxy.image?.let {
                if ((it.format == ImageFormat.YUV_420_888
                            || it.format == ImageFormat.YUV_422_888
                            || it.format == ImageFormat.YUV_444_888)
                    && it.planes.size == 3) {

                    val buffer = it.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)
                    // Create a LuminanceSource.
                    val rotatedImage = RotatedImage(bytes, imageProxy.width, imageProxy.height)

                    rotateImageArray(rotatedImage, rotationDegrees)

                    val source = PlanarYUVLuminanceSource(rotatedImage.byteArray,
                        rotatedImage.width,
                        rotatedImage.height,
                        0,
                        0,
                        rotatedImage.width,
                        rotatedImage.height,
                        false)

                    // Create a Binarizer
                    val binarizer = HybridBinarizer(source)
                    // Create a BinaryBitmap.
                    val binaryBitmap = BinaryBitmap(binarizer)
                    // Try decoding...
                    val result: Result
                    try {
                        result = reader.decode(binaryBitmap)
                        onQrCodesDetected(result)
                    } catch (e: NotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    // Manage other image formats
                    // TODO - https://developer.android.com/reference/android/media/Image.html
                }
            }
        } catch (ise: IllegalStateException) {
            ise.printStackTrace()
        }
        imageProxy.close()
    }

    // 90, 180. 270 rotation
    private fun rotateImageArray(imageToRotate: RotatedImage, rotationDegrees: Int) {
        if (rotationDegrees == 0) return // no rotation
        if (rotationDegrees % 90 != 0) return // only 90 degree times rotations

        val width = imageToRotate.width
        val height = imageToRotate.height

        val rotatedData = ByteArray(imageToRotate.byteArray.size)
        for (y in 0 until height) { // we scan the array by rows
            for (x in 0 until width) {
                when (rotationDegrees) {
                    90 -> rotatedData[x * height + height - y - 1] =
                        imageToRotate.byteArray[x + y * width]
                    180 -> rotatedData[width * (height - y - 1) + width - x - 1] =
                        imageToRotate.byteArray[x + y * width]
                    270 -> rotatedData[x + y * width] =
                        imageToRotate.byteArray[x * height + height - y - 1]
                }
            }
        }

        imageToRotate.byteArray = rotatedData

        if (rotationDegrees != 180) {
            imageToRotate.height = width
            imageToRotate.width = height
        }
    }
}

private data class RotatedImage(var byteArray: ByteArray, var width: Int, var height: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RotatedImage

        if (!byteArray.contentEquals(other.byteArray)) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = byteArray.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}