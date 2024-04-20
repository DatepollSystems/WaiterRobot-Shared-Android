package org.datepollsystems.waiterrobot.shared.features.billing.models

import org.datepollsystems.waiterrobot.shared.utils.Money
import org.datepollsystems.waiterrobot.shared.utils.times

data class BillItem(
    val productId: Long,
    val name: String,
    val ordered: Int,
    val selectedForBill: Int,
    val pricePerPiece: Money,
    val orderProductIds: List<Long> = emptyList(),
) {
    val priceSum: Money get() = selectedForBill * pricePerPiece
}
