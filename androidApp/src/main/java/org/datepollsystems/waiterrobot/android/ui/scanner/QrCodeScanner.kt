package org.datepollsystems.waiterrobot.android.ui.scanner

import android.Manifest
import android.content.Context
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.util.QrCodeAnalyzer
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.cameraPermissionRequired
import org.datepollsystems.waiterrobot.shared.generated.localization.errorOpeningCamera
import org.datepollsystems.waiterrobot.shared.generated.localization.noCameraFound
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrCodeScanner(onResult: (Barcode) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var errorMessage: String? by remember { mutableStateOf(null) }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(true) {
        // Launch by default as for opening the scanner always a button must be clicked
        if (!cameraPermissionState.status.isGranted) cameraPermissionState.launchPermissionRequest()
    }

    Box {
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        } else if (!cameraPermissionState.status.isGranted) {
            Text(
                text = L.qrScanner.cameraPermissionRequired(),
                textAlign = TextAlign.Center
            )
        } else {
            var flashLight by remember { mutableStateOf(false) }
            var cam: Camera? by remember { mutableStateOf(null) }

            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)
                    val previewUseCase = Preview.Builder().build()
                        .apply { setSurfaceProvider(previewView.surfaceProvider) }

                    val analysisUseCase = ImageAnalysis.Builder()
                        .setTargetResolution(Size(previewView.width, previewView.height))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) // Do not process every frame only keep the latest
                        .build()
                        .apply {
                            setAnalyzer(
                                Executors.newSingleThreadExecutor(), // Execute analysis on a single worker thread
                                QrCodeAnalyzer {
                                    this.clearAnalyzer() // Stop after first detected code // TODO test scanning invalid code (no data and no url)
                                    onResult(it.first())
                                }
                            )
                        }

                    coroutineScope.launch {
                        try {
                            val cameraProvider = context.getCameraProvider()
                            // Make sure no use case is bound to the cameraProvider, when QrCodeScanner is "launched" twice
                            // (e.g. when first scan at login fails and the QrCodeScanner is then opened a second time)

                            if (!cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                                errorMessage = L.qrScanner.noCameraFound()
                                return@launch
                            }

                            cameraProvider.unbindAll()
                            cam = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                previewUseCase,
                                analysisUseCase
                            )
                        } catch (e: Exception) {
                            errorMessage = L.qrScanner.errorOpeningCamera()
                            // TODO Logger.error
                            e.printStackTrace()
                        }
                    }
                    previewView
                }
            )

            if (cam == null) {
                CircularProgressIndicator()
            } else if (cam?.cameraInfo?.hasFlashUnit() == true) {
                LaunchedEffect(flashLight) {
                    cam?.cameraControl?.enableTorch(flashLight)
                }

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    onClick = { flashLight = !flashLight }
                ) {
                    Icon(
                        imageVector = if (flashLight) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                        contentDescription = "Flashlight",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
