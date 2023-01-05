package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.models.Product

data class OrderState(
    override val viewState: ViewState = ViewState.Idle,
    val showConfirmationDialog: Boolean = false,
    internal val _products: List<Product> = emptyList(),
    internal val _currentOrder: Map<Long, OrderItem> = emptyMap() // Product ID to Order
) : ViewModelState() {
    // Expose only as a list of OrderItems
    val currentOrder: List<OrderItem> by lazy {
        _currentOrder.values.toList()
    }

    // Put sold out products to the very end
    val products: List<Product> by lazy {
        _products.sortedBy(Product::soldOut)
    }

    override fun withViewState(viewState: ViewState): OrderState = copy(viewState = viewState)
}
