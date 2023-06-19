package org.datepollsystems.waiterrobot.shared.features.order.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.order.api.models.ProductGroupResponseDto

internal class ProductApi(client: AuthorizedClient) : AuthorizedApi("waiter/product", client) {

    suspend fun getProducts(eventId: Long) =
        get("/", "eventId" to eventId.toString()).body<List<ProductGroupResponseDto>>()
}
