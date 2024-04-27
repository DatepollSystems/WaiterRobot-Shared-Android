package org.datepollsystems.waiterrobot.shared.features.billing.models

import org.datepollsystems.waiterrobot.shared.utils.Money
import org.datepollsystems.waiterrobot.shared.utils.times

data class BillItem(
    val baseProductId: Long,
    val name: String,
    val ordered: Int,
    val selectedForBill: Int,
    val pricePerPiece: Money,
    val orderProductIds: List<Long> = emptyList(),
) {
    val priceSum: Money get() = selectedForBill * pricePerPiece

    // Take the first orderProduct id as a identifier for this billItem
    val virtualId: Long get() = orderProductIds.first()
}
