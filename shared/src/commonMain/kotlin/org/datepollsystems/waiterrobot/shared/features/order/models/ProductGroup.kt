package org.datepollsystems.waiterrobot.shared.features.order.models

data class ProductGroup(
    val id: Long,
    val name: String,
    val position: Int,
    val color: String?,
    val products: List<Product>
)
