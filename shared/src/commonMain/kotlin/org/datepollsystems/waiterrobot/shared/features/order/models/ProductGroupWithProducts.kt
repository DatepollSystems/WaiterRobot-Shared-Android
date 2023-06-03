package org.datepollsystems.waiterrobot.shared.features.order.models

data class ProductGroupWithProducts(
    val group: ProductGroup,
    val products: List<Product>
)
