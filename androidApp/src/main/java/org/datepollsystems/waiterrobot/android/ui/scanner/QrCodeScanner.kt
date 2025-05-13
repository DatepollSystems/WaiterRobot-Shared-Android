package org.datepollsystems.waiterrobot.android.ui.scanner

import android.Manifest
import android.content.Context
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.util.QrCodeAnalyzer
import org.datepollsystems.waiterrobot.android.util.getLogger
import org.datepollsystems.waiterrobot.shared.localization.MR
import java.util.concurrent.Executors
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrCodeScanner(
    modifier: Modifier = Modifier,
    onResult: (Barcode) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val logger = getLogger("QrCodeScanner")

    var errorMessage: StringResource? by remember { mutableStateOf(null) }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(true) {
        // Launch by default as for opening the scanner always a button must be clicked
        if (!cameraPermissionState.status.isGranted) cameraPermissionState.launchPermissionRequest()
    }

    Box(modifier) {
        if (errorMessage != null) {
            Text(
                text = errorMessage!!(),
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        } else if (!cameraPermissionState.status.isGranted) {
            Text(
                text = MR.strings.qrScanner_cameraPermissionRequired(),
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        } else {
            var flashLight by remember { mutableStateOf(false) }
            var cam: Camera? by remember { mutableStateOf(null) }

            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context).apply {
                        clipToOutline = true
                    }
                    val previewUseCase = Preview.Builder().build()
                        .apply { surfaceProvider = previewView.surfaceProvider }

                    val analysisUseCase = ImageAnalysis.Builder()
                        .setResolutionSelector(
                            ResolutionSelector.Builder().setResolutionStrategy(
                                ResolutionStrategy(
                                    Size(previewView.width, previewView.height),
                                    ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                                )
                            ).build()
                        )
                        // Do not process every frame only keep the latest
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(
                                // Execute analysis on a single worker thread
                                Executors.newSingleThreadExecutor(),
                                QrCodeAnalyzer {
                                    // Stop after first detected code
                                    // TODO test scanning invalid code (no data and no url)
                                    this.clearAnalyzer()
                                    onResult(it.first())
                                }
                            )
                        }

                    coroutineScope.launch {
                        @Suppress("TooGenericExceptionCaught")
                        try {
                            val cameraProvider = context.getCameraProvider()
                            if (!cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                                errorMessage = MR.strings.qrScanner_noCameraFound
                                return@launch
                            }

                            // Make sure no use case is bound to the cameraProvider,
                            // when QrCodeScanner is "launched" twice (e.g. when first scan at
                            // login fails and the QrCodeScanner is then opened a second time)
                            cameraProvider.unbindAll()

                            cam = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                previewUseCase,
                                analysisUseCase
                            )
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            errorMessage = MR.strings.qrScanner_errorOpeningCamera
                            logger.e(e) { "Failed to open camera" }
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
            @Suppress("BlockingMethodInNonBlockingContext")
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
