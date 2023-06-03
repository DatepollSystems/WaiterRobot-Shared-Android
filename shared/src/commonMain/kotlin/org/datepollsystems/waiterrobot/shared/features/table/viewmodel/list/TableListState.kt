package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroupWithTables

data class TableListState(
    val filteredTableGroups: List<TableGroupWithTables> = emptyList(),
    val selectedTableGroups: Set<TableGroup> = emptySet(),
    val unselectedTableGroups: Set<TableGroup> = emptySet(),
    override val viewState: ViewState = ViewState.Loading
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): TableListState = copy(viewState = viewState)
}
