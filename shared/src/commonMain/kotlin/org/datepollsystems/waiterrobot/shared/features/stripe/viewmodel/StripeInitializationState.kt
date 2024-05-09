package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.disabledForEvent
import org.datepollsystems.waiterrobot.shared.generated.localization.locationPermissionDenied
import org.datepollsystems.waiterrobot.shared.generated.localization.readerConnectionFailed
import org.datepollsystems.waiterrobot.shared.generated.localization.terminalInitiationFailed

data class StripeInitializationState(
    val step: Step = Step.Start,
    val isLoading: Boolean = false,
    @Deprecated("Legacy - Not used anymore")
    override val viewState: ViewState = ViewState.Idle,
) : ViewModelState() {

    sealed class Step(val stepIndex: Int) {
        data object Start : Step(1)
        data object GrantLocationPermission : Step(2)
        data object EnableGeoLocation : Step(3)
        data object EnableNfc : Step(4)
        data object Finished : Step(5)
        sealed class Error(val description: String, val retryAble: Boolean = true) : Step(0) {
            data object StripeDisabledForEvent :
                Error(L.stripeInit.error.disabledForEvent(), retryAble = false)

            data object GeolocationPermissionDenied :
                Error(L.stripeInit.error.locationPermissionDenied())

            data object TerminalInitializationFailed :
                Error(L.stripeInit.error.terminalInitiationFailed())

            data object ReaderConnectionFailed : Error(L.stripeInit.error.readerConnectionFailed())
        }

        companion object {
            const val COUNT: Float = 5f
        }
    }

    @Deprecated("Legacy - Not used anymore")
    override fun withViewState(viewState: ViewState): ViewModelState = copy(viewState = viewState)
}
