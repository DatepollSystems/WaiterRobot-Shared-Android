package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.mapType
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import kotlin.native.HiddenFromObjC

data class TableListState(
    @HiddenFromObjC
    val tableGroups: Resource<List<TableGroup>> = Resource.Loading(),
    @Deprecated("Legacy - Not used anymore")
    override val viewState: ViewState = ViewState.Loading
) : ViewModelState() {

    @Suppress("unused") // iOS only
    val tableGroupsArray by lazy {
        tableGroups.mapType { it?.toTypedArray() }
    }

    @Deprecated("Legacy - Not used anymore")
    @Suppress("DeprecatedCallableAddReplaceWith")
    override fun withViewState(viewState: ViewState): TableListState = copy(viewState = viewState)
}
