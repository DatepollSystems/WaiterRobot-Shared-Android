package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroupWithTables

data class TableListState(
    val tableGroups: Resource<List<TableGroup>> = Resource.Loading(),
    val filteredTableGroups: List<TableGroupWithTables> = emptyList(),
    internal val selectedTableGroups: Set<TableGroup> = emptySet(),
    internal val unselectedTableGroups: Set<TableGroup> = emptySet(),
    override val viewState: ViewState = ViewState.Loading
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): TableListState = copy(viewState = viewState)

    val selectedTableGroupList: List<TableGroup> =
        selectedTableGroups.sortedBy { it.name.lowercase() }
    val unselectedTableGroupList: List<TableGroup> =
        unselectedTableGroups.sortedBy { it.name.lowercase() }
}
