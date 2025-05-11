package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.localization.MR

data class StripeInitializationState(
    val step: Step = Step.Start,
    val stepIndex: Int = 1,
    val isLoading: Boolean = false,
) : ViewModelState {

    sealed class Step {
        data object Start : Step()
        data object GrantLocationPermission : Step()
        data object EnableGeoLocation : Step()
        data object EnableNfc : Step()
        data object Finished : Step()
        sealed class Error(val description: StringDesc, val retryAble: Boolean = true) : Step() {
            data object StripeDisabledForEvent :
                Error(MR.strings.stripeInit_error_disabled_forEvent.desc(), retryAble = false)

            data object GeolocationPermissionDenied :
                Error(MR.strings.stripeInit_error_location_denied.desc())

            data object TerminalInitializationFailed :
                Error(MR.strings.stripeInit_error_terminal_init.desc())

            data object ReaderConnectionFailed :
                Error(MR.strings.stripeInit_error_reader_connection.desc())
        }

        companion object {
            const val COUNT: Float = 6f
        }
    }
}
