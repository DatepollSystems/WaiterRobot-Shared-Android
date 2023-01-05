package org.datepollsystems.waiterrobot.shared.features.order.api.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.utils.Cents

@Serializable
internal class ProductGroupResponseDto(
    val id: Long,
    val name: String,
    val products: List<ProductDto>
)

@Serializable
internal class ProductDto(
    val id: Long,
    val name: String,
    val soldOut: Boolean,
    val price: Cents,
    val allergens: List<AllergenDto>
)

@Serializable
internal class AllergenDto(
    val id: Long,
    val name: String,
    val shortName: String
)
