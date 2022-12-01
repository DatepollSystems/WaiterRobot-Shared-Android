package org.datepollsystems.waiterrobot.shared.root

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState

data class RootState(
    override val viewState: ViewState = ViewState.Idle
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): RootState = copy(viewState = viewState)
}
