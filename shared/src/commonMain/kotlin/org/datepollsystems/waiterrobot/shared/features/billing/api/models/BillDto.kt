package org.datepollsystems.waiterrobot.shared.features.billing.api.models

import kotlinx.serialization.Serializable

@Serializable
internal class BillResponseDto(
    val tableId: Long,
    val tableNumber: Long,
    val products: List<BillItem>
) {
    @Serializable
    class BillItem(
        val id: Long,
        val amount: Int,
        val name: String,
    )
}
