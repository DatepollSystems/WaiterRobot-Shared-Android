package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.mapType
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroup
import kotlin.native.HiddenFromObjC
import kotlin.native.ObjCName

data class OrderState(
    override val viewState: ViewState = ViewState.Idle,
    @HiddenFromObjC
    val productGroups: Resource<List<ProductGroup>> = Resource.Loading(),
    @Suppress("ConstructorParameterNaming", "PropertyName")
    internal val _currentOrder: Resource<Map<Long, OrderItem>> = Resource.Success(emptyMap()), // Product ID to Order
    internal val filter: String = ""
) : ViewModelState() {

    // Expose only as a list of OrderItems
    @HiddenFromObjC
    val currentOrder: Resource<List<OrderItem>> by lazy {
        _currentOrder.mapType { orders ->
            orders?.values?.sortedBy { it.product.position }?.toList()
        }
    }

    @Suppress("unused") // iOS only
    @ObjCName("currentOrder")
    val currentOrderArray: Resource<Array<OrderItem>> by lazy {
        currentOrder.mapType { it?.toTypedArray() }
    }

    @Suppress("unused") // iOS only
    @ObjCName("productGroups")
    val productGroupsArray: Resource<Array<ProductGroup>> by lazy {
        productGroups.mapType { it?.toTypedArray() }
    }

    @Suppress("unused") // iOS only
    val hasSelectedItems: Boolean by lazy { !currentOrder.data.isNullOrEmpty() }

    override fun withViewState(viewState: ViewState): OrderState = copy(viewState = viewState)
}
