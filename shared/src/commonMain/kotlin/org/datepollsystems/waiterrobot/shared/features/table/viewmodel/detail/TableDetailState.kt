package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderedItem

data class TableDetailState(
    override val viewState: ViewState = ViewState.Idle,
    val orderedItemsResource: Resource<List<OrderedItem>> = Resource.Loading()
) : ViewModelState() {
    override fun withViewState(viewState: ViewState): TableDetailState = copy(viewState = viewState)
}
