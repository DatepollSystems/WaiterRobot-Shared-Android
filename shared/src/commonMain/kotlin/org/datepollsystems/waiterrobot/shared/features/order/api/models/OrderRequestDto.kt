package org.datepollsystems.waiterrobot.shared.features.order.api.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.api.RequestBodyDto

@Serializable
class OrderRequestDto(
    val tableId: Long,
    val products: List<OrderItemDto>
) : RequestBodyDto {
    @Serializable
    class OrderItemDto(
        val id: Long,
        val amount: Int,
        val note: String?
    )
}
