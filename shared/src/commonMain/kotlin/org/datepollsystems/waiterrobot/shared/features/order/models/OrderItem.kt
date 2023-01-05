package org.datepollsystems.waiterrobot.shared.features.order.models

data class OrderItem(
    val product: Product,
    val amount: Int,
    val note: String?
)
