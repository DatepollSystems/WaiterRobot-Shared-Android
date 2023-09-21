package org.datepollsystems.waiterrobot.shared.features.order.models

data class ProductGroup(
    val id: Long,
    val name: String,
    val position: Int,
    val products: List<Product>
)
