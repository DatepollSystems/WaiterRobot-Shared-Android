package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.mapType
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroup

data class OrderState(
    override val viewState: ViewState = ViewState.Idle,
    val productGroups: Resource<List<ProductGroup>> = Resource.Loading(),
    @Suppress("ConstructorParameterNaming", "PropertyName")
    internal val _currentOrder: Resource<Map<Long, OrderItem>> = Resource.Success(emptyMap()), // Product ID to Order
    internal val filter: String = ""
) : ViewModelState() {

    // Expose only as a list of OrderItems
    val currentOrder: Resource<List<OrderItem>> by lazy {
        _currentOrder.mapType { orders ->
            orders?.values?.sortedBy { it.product.position }?.toList()
        }
    }

    override fun withViewState(viewState: ViewState): OrderState = copy(viewState = viewState)
}
