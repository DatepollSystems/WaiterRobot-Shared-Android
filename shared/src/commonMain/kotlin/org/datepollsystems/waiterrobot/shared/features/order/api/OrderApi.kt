package org.datepollsystems.waiterrobot.shared.features.order.api

import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.order.api.models.OrderRequestDto

internal class OrderApi(client: AuthorizedClient) : AuthorizedApi("waiter/order", client) {
    suspend fun sendOrder(order: OrderRequestDto) {
        post("/", order)
    }
}
