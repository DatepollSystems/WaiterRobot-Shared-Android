package org.datepollsystems.waiterrobot.shared.features.order.api

import io.ktor.client.*
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.features.order.api.models.OrderRequestDto

internal class OrderApi(client: HttpClient) : AbstractApi("waiter/order", client) {
    suspend fun sendOrder(order: OrderRequestDto) {
        post("/", order)
    }
}

