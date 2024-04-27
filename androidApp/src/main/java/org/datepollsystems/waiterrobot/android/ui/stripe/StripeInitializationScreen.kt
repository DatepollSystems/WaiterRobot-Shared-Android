package org.datepollsystems.waiterrobot.android.ui.stripe

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.util.PermissionsControllerBindEffect
import org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel.StripeInitializationState
import org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel.StripeInitializationViewModel
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
fun StripeInitializationScreen(
    navigator: NavController,
    vm: StripeInitializationViewModel = koinViewModel()
) {
    val state by vm.collectAsState()
    vm.handleSideEffects(navigator)

    PermissionsControllerBindEffect(vm.permissionsController)

    when (val initializationState = state.initializationState) {
        null -> {
            Column {
                Text(
                    text = "Your event is set up for contactless payment. " +
                        "To accept contactless payments you need to enable the NFC functionality on your device." +
                        "Additionally to ensure secure payments access to the location is required. " +
                        "Please enable NFC and location services on your device and allow the app to access your location."
                )
                Button(onClick = vm::enableStripe) {
                    Text("Enable Contactless Payment")
                }
                Button(onClick = vm::onContinueClick) {
                    Text("Continue without Contactless Payment")
                }
            }
        }

        is StripeInitializationState.State.Error -> {
            Column {
                Text(text = "An error occurred while initializing contactless payment.")
                Text(text = initializationState.description)
                Button(onClick = vm::enableStripe) {
                    Text(text = "Retry")
                }
                Button(onClick = vm::onContinueClick) {
                    Text("Continue without Contactless Payment")
                }
            }
        }

        StripeInitializationState.State.Finished -> {
            Column {
                Text(text = "Contactless payment is initialized.")
                Button(onClick = vm::onContinueClick) {
                    Text(text = "Let's go!")
                }
            }
        }

        else -> {
            Column {
                Text(text = "Initializing contactless payment...")
                CircularProgressIndicator(progress = { initializationState.progress / 100f })
                Text(text = initializationState.description)
            }
        }
    }
}
