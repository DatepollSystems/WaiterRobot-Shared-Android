package org.datepollsystems.waiterrobot.shared.features.order.api

import io.ktor.client.*
import io.ktor.client.call.*
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.features.order.api.models.ProductGroupResponseDto

internal class ProductApi(client: HttpClient) : AbstractApi("waiter/product", client) {

    suspend fun getProducts(eventId: Long) =
        get("/", "eventId" to eventId.toString()).body<List<ProductGroupResponseDto>>()
}
