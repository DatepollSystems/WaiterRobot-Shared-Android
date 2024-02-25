package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.mapType
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderedItem
import kotlin.native.ObjCName

data class TableDetailState(
    override val viewState: ViewState = ViewState.Idle,
    val orderedItemsResource: Resource<List<OrderedItem>> = Resource.Loading()
) : ViewModelState() {

    @Suppress("unused") // iOS only
    @ObjCName("orderedItems")
    val orderedItemsArray by lazy {
        orderedItemsResource.mapType { it?.toTypedArray() }
    }

    override fun withViewState(viewState: ViewState): TableDetailState = copy(viewState = viewState)
}
