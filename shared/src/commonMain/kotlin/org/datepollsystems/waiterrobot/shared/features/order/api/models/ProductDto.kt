package org.datepollsystems.waiterrobot.shared.features.order.api.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.utils.Cents

@Serializable
internal class ProductGroupDto(
    val id: Long,
    val name: String,
    val color: String?,
    val products: List<ProductDto>,
    val position: Int = Int.MAX_VALUE,
)

@Serializable
internal class ProductDto(
    val id: Long,
    val name: String,
    val soldOut: Boolean,
    val price: Cents,
    val allergens: List<AllergenDto>,
    val position: Int = Int.MAX_VALUE,
)

@Serializable
internal class AllergenDto(
    val id: Long,
    val name: String,
    val shortName: String
)
