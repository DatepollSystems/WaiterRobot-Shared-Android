package org.datepollsystems.waiterrobot.shared.root

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState

data class RootState(
    val isLoggedIn: Boolean = CommonApp.isLoggedIn.value,
    val hasEventSelected: Boolean = CommonApp.hasEventSelected.value,
    override val viewState: ViewState = ViewState.Idle
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): RootState = copy(viewState = viewState)
}
