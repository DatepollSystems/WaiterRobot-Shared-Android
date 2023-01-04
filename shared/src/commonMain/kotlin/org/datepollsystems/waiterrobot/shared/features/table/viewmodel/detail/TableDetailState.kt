package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderItem

data class TableDetailState(
    override val viewState: ViewState = ViewState.Idle,
    val products: List<OrderItem> = emptyList()
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): TableDetailState = copy(viewState = viewState)
}
