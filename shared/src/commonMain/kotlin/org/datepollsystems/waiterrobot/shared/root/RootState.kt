package org.datepollsystems.waiterrobot.shared.root

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event

data class RootState(
    val isLoggedIn: Boolean = CommonApp.isLoggedIn.value,
    val hasEventSelected: Boolean = CommonApp.selectedEvent.value != null,
    val needsStripeInitialization: Boolean =
        CommonApp.settings.selectedEvent?.stripeSettings is Event.StripeSettings.Enabled &&
            CommonApp.stripeProvider?.connectedToReader?.value == false,
    val selectedTheme: AppTheme = CommonApp.appTheme.value,
    override val viewState: ViewState = ViewState.Idle
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): RootState = copy(viewState = viewState)
}
