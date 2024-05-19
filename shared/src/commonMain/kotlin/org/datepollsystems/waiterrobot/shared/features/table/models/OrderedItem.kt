package org.datepollsystems.waiterrobot.shared.features.table.models

data class OrderedItem(
    val baseProductId: Long,
    val name: String,
    val amount: Int,
    val virtualId: Long
)
