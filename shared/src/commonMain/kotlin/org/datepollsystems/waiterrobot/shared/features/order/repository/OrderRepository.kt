package org.datepollsystems.waiterrobot.shared.features.order.repository

import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.order.api.OrderApi
import org.datepollsystems.waiterrobot.shared.features.order.api.models.OrderRequestDto
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

internal class OrderRepository(private val orderApi: OrderApi) : AbstractRepository() {

    suspend fun sendOrder(table: Table, order: List<OrderItem>, orderId: String) {
        val items = order.map { OrderRequestDto.OrderItemDto(it.product.id, it.amount, it.note) }
        orderApi.sendOrder(OrderRequestDto(table.id, items, orderId))
    }
}
