package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

data class TableListState(
    val tables: List<Table> = emptyList(),
    override val viewState: ViewState = ViewState.Loading
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): TableListState = copy(viewState = viewState)
}
