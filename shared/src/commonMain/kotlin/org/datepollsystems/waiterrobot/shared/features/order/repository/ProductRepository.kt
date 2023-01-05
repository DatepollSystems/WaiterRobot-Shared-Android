package org.datepollsystems.waiterrobot.shared.features.order.repository

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.order.api.ProductApi
import org.datepollsystems.waiterrobot.shared.features.order.models.Allergen
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroup
import org.datepollsystems.waiterrobot.shared.utils.cent

internal class ProductRepository(private val productApi: ProductApi) : AbstractRepository() {

    suspend fun getProductById(id: Long): Product? {
        // TODO get from db and limit force update
        return getProducts(true).find { it.id == id }
    }

    suspend fun getProducts(forceUpdate: Boolean = false): List<Product> {
        val eventId = CommonApp.settings.selectedEventId

        logger.i { "Loading products from api ..." }
        val apiResponse = productApi.getProducts(eventId)
        val groups = apiResponse.associate { it.id to ProductGroup(it.id, it.name) }
        val apiProducts = productApi.getProducts(eventId).flatMap { group ->
            group.products.map {
                Product(
                    id = it.id,
                    name = it.name,
                    price = it.price.cent,
                    soldOut = it.soldOut,
                    allergens = it.allergens.map { allergen ->
                        Allergen(allergen.id, allergen.name, allergen.shortName)
                    },
                    productGroup = groups[group.id]!!
                )
            }
        }
        logger.d { "Got ${apiProducts.count()} products from api" }

        return apiProducts
    }
}
