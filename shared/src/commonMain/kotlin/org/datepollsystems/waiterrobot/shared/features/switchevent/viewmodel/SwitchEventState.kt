package org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event

data class SwitchEventState(
    override val viewState: ViewState = ViewState.Loading,
    val events: List<Event> = emptyList()
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): SwitchEventState = copy(viewState = viewState)
}
