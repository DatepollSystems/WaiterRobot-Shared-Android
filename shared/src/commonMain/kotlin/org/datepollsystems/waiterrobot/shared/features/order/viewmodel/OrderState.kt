package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroup

data class OrderState(
    override val viewState: ViewState = ViewState.Idle,
    val showConfirmationDialog: Boolean = false,
    val productGroups: List<ProductGroup> = emptyList(),
    @Suppress("ConstructorParameterNaming")
    internal val _currentOrder: Map<Long, OrderItem> = emptyMap() // Product ID to Order
) : ViewModelState() {
    // Expose only as a list of OrderItems
    val currentOrder: List<OrderItem> by lazy {
        _currentOrder.values.toList()
    }

    override fun withViewState(viewState: ViewState): OrderState = copy(viewState = viewState)
}
