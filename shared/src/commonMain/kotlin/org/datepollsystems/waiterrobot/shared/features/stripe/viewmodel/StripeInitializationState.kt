package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState

data class StripeInitializationState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    @Deprecated("Legacy - Not used anymore")
    override val viewState: ViewState = ViewState.Idle,
) : ViewModelState() {
    enum class Error {
        GEOLOCATION_PERMISSION_DENIED,
        GEOLOCATION_NOT_ENABLED
    }

    @Deprecated("Legacy - Not used anymore")
    override fun withViewState(viewState: ViewState): ViewModelState = copy(viewState = viewState)
}
