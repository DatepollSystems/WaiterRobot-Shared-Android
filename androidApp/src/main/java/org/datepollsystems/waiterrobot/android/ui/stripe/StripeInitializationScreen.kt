package org.datepollsystems.waiterrobot.android.ui.stripe

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.util.PermissionsControllerBindEffect
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel.StripeInitializationState
import org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel.StripeInitializationViewModel
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
fun StripeInitializationScreen(
    navigator: NavController,
    vm: StripeInitializationViewModel = koinViewModel()
) {
    val state by vm.collectAsState()
    vm.handleSideEffects(navigator)

    PermissionsControllerBindEffect(vm.permissionsController)

    val event by CommonApp.selectedEvent.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) },
        topBar = {
            TopAppBar(
                title = { Text(MR.strings.stripeInit_title()) },
                navigationIcon = {
                    if (navigator.previousBackStackEntry != null) {
                        IconButton(onClick = { navigator.popBackStack() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = MR.strings.navigation_back()
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { state.stepIndex / StripeInitializationState.Step.COUNT },
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                }

                when (val step = state.step) {
                    StripeInitializationState.Step.Start -> {
                        Text(
                            text = MR.strings.stripeInit_step_start_desc(event?.name ?: "UNKNOWN"),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = MR.strings.stripeInit_locationDataSharingNotice(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Button(
                            onClick = vm::startInitialization,
                            enabled = !state.isLoading
                        ) {
                            Text(MR.strings.stripeInit_step_start_action())
                        }
                    }

                    StripeInitializationState.Step.GrantLocationPermission -> {
                        Text(
                            text = MR.strings.stripeInit_step_grantLocationPermission_desc(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            onClick = vm::grantLocationPermission,
                            enabled = !state.isLoading
                        ) {
                            Text(MR.strings.stripeInit_step_grantLocationPermission_action())
                        }
                    }

                    StripeInitializationState.Step.EnableGeoLocation -> {
                        val intentSenderRequestLauncher = rememberLauncherForActivityResult(
                            ActivityResultContracts.StartIntentSenderForResult()
                        ) {
                            vm.enableGeoLocation()
                        }
                        val intentLauncher = rememberLauncherForActivityResult(
                            ActivityResultContracts.StartActivityForResult()
                        ) {
                            vm.enableGeoLocation()
                        }
                        val context: Context = LocalContext.current
                        Text(
                            text = MR.strings.stripeInit_step_enableGeoLocation_desc(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            enabled = !state.isLoading,
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    checkLocationSetting(
                                        context = context,
                                        onDisabled = intentSenderRequestLauncher::launch,
                                        onEnabled = vm::enableGeoLocation
                                    )
                                } else {
                                    intentLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                }
                            }
                        ) {
                            Text(MR.strings.stripeInit_step_enableGeoLocation_action())
                        }
                    }

                    StripeInitializationState.Step.EnableNfc -> {
                        val launcher = rememberLauncherForActivityResult(
                            ActivityResultContracts.StartActivityForResult()
                        ) {
                            vm.enableNfc()
                        }
                        Text(
                            text = MR.strings.stripeInit_step_enableNfc_desc(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            enabled = !state.isLoading,
                            onClick = {
                                launcher.launch(Intent(Settings.ACTION_NFC_SETTINGS))
                            }
                        ) {
                            Text(MR.strings.stripeInit_step_enableNfc_action())
                        }
                    }

                    is StripeInitializationState.Step.Error -> {
                        Text(
                            text = MR.strings.stripeInit_step_error_desc(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = step.description(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        if (step.retryAble) {
                            Button(
                                enabled = !state.isLoading,
                                onClick = vm::startInitialization
                            ) {
                                Text(MR.strings.stripeInit_step_error_action())
                            }
                        }
                    }

                    StripeInitializationState.Step.Finished -> {
                        Text(
                            text = MR.strings.stripeInit_step_finished_desc(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Button(onClick = { vm.onContinueClick(skipInit = false) }) {
                            Text(MR.strings.stripeInit_step_finished_action())
                        }
                    }
                }

                if (state.step != StripeInitializationState.Step.Finished) {
                    TextButton(onClick = { vm.onContinueClick(skipInit = true) }) {
                        Text(MR.strings.stripeInit_continue_without())
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {
    @Suppress("MagicNumber")
    val gpsSettingTask: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(context)
        .checkLocationSettings(
            LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.Builder(10000).build())
                .build()
        )

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            @Suppress("SwallowedException")
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }
}
