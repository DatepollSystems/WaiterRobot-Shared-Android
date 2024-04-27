package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState

data class StripeInitializationState(
    val initializationState: State? = null,
    @Deprecated("Legacy - Not used anymore")
    override val viewState: ViewState = ViewState.Idle,
) : ViewModelState() {

    sealed class State(val description: String, val progress: Int) {
        data object Started : State("Initializing", 0)
        data object CheckingPermissions : State("Checking permissions", 10)
        data object InitializingTerminal : State("Initializing contactless payment terminal", 30)
        data object ConnectingReader : State("Initializing card reader module of the phone", 60)
        data object Finished : State("Initialization finished", 100)
        sealed class Error(description: String) : State(description, 0) {
            data object NoEventSelected : Error("No event selected")
            data object StripeDisabledForEvent : Error("Stripe is disabled for this event")
            data object GeolocationPermissionDenied : Error("Location permission not granted")
            data object GeolocationNotEnabled : Error("Location services not enabled")
            data object NfcNotEnabled : Error("NFC is not enabled")
            data object TerminalInitializationFailed :
                Error("Could not initialize contactless payment terminal")

            data object ReaderConnectionFailed :
                Error("Could not initialize card reader module of the phone")
        }
    }

    @Deprecated("Legacy - Not used anymore")
    override fun withViewState(viewState: ViewState): ViewModelState = copy(viewState = viewState)
}
