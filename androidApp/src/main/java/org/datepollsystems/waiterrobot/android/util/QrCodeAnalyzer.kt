package org.datepollsystems.waiterrobot.android.util

import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import co.touchlab.kermit.Logger
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class QrCodeAnalyzer(
    private val onQrCode: (List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer, KoinComponent {
    private val logger: Logger by inject()

    private var lastAnalyzedTimeStamp = 0L

    private val qrCodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
    )

    override fun analyze(image: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimeStamp >= TimeUnit.SECONDS.toMillis(1)) {
            val imageToAnalyze = image.safeGetImage()
            if (imageToAnalyze != null) {
                val imageToProcess =
                    InputImage.fromMediaImage(imageToAnalyze, image.imageInfo.rotationDegrees)

                qrCodeScanner.process(imageToProcess)
                    .addOnSuccessListener { barcodeList ->
                        if (barcodeList.isNotEmpty()) { // List is empty if no code was recognized
                            onQrCode(barcodeList)
                        }
                    }
                    .addOnFailureListener {
                        logger.e("Qr code scanner processing failed", it)
                    }
                    .addOnCompleteListener {
                        image.close() // Image proxy must be closed to continue scanning with the next image
                    }
            } else {
                // Shouldn't happen, but log anyways because the api is "experimental"
                logger.e { "Couldn't get image from imageProxy: $this" }
            }
            lastAnalyzedTimeStamp = currentTimestamp
        } else {
            image.close()
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun ImageProxy.safeGetImage(): Image? =
        this.image // Image is Nullable (but should not happen)
}
