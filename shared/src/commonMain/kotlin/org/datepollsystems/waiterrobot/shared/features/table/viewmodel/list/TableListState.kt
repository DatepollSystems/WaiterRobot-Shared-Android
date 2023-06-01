package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup

data class TableListState(
    val filteredTables: List<Table> = emptyList(),
    val selectedTableGroups: Set<TableGroup> = emptySet(),
    val unselectedTableGroups: Set<TableGroup> = emptySet(),
    override val viewState: ViewState = ViewState.Loading
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): TableListState = copy(viewState = viewState)
}
