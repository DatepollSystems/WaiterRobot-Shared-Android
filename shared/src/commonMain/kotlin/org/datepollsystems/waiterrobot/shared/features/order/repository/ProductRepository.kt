package org.datepollsystems.waiterrobot.shared.features.order.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.order.api.ProductApi
import org.datepollsystems.waiterrobot.shared.features.order.api.models.ProductDto
import org.datepollsystems.waiterrobot.shared.features.order.api.models.ProductGroupDto
import org.datepollsystems.waiterrobot.shared.features.order.db.ProductDatabase
import org.datepollsystems.waiterrobot.shared.features.order.db.model.AllergenEntry
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductEntry
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductGroupEntry
import org.datepollsystems.waiterrobot.shared.features.order.models.Allergen
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroup
import org.datepollsystems.waiterrobot.shared.utils.cent
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import org.datepollsystems.waiterrobot.shared.utils.extensions.olderThan
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.hours

internal class ProductRepository : AbstractRepository(), KoinComponent {
    private val productApi: ProductApi by inject()
    private val productDb: ProductDatabase by inject()
    private val coroutineScope: CoroutineScope by inject()

    init {
        // Delete outdated at app start
        coroutineScope.launch {
            productDb.deleteOlderThan(maxAge)
        }
    }

    suspend fun getProductById(id: Long): Product? {
        return productDb.getProductById(id)?.toModel()
            ?: getProductGroups(true) // TODO limit force update
                .flatMap(ProductGroup::products)
                .find { it.id == id }
    }

    suspend fun getProductGroups(forceUpdate: Boolean = false): List<ProductGroup> {
        val eventId = CommonApp.settings.selectedEventId

        fun loadFromDb(): List<ProductGroup>? {
            logger.i { "Fetching products from DB ..." }
            val dbProducts = productDb.getForEvent(eventId)
            logger.d { "Found ${dbProducts.count()} products in DB" }

            return if (dbProducts.isEmpty() || dbProducts.any { it.updated.olderThan(maxAge) }) {
                null
            } else {
                dbProducts.map(ProductGroupEntry::toModel)
            }
        }

        suspend fun loadFromApiAndStore(): List<ProductGroup> {
            logger.i { "Loading products from api ..." }

            val timestamp = Now()
            val apiProducts = productApi.getProducts(eventId)
            logger.d { "Got ${apiProducts.sumOf { it.products.count() }} products from api" }

            val modelGroups = apiProducts.map(ProductGroupDto::toModel)
            val entryGroups = apiProducts.map { it.toEntry(eventId, timestamp) }

            logger.i { "Remove old products from DB ..." }
            productDb.deleteForEvent(eventId)

            logger.i { "Saving products to DB ..." }
            productDb.insert(entryGroups)

            return modelGroups
        }

        val result = when (forceUpdate) {
            true -> loadFromApiAndStore()
            false -> loadFromDb() ?: loadFromApiAndStore()
        }

        return result
            .filter { it.products.isNotEmpty() } // Do not show groups that do not have products at all
            .sortedBy { it.name.lowercase() } // Sort groups with same position by name
            .sortedBy(ProductGroup::position)
    }

    companion object {
        private val maxAge = 24.hours
    }
}

private fun ProductGroupDto.toModel() = ProductGroup(
    id = this.id,
    name = this.name,
    position = this.position,
    products = this.products
        .map(ProductDto::toModel)
        .sortedBy { it.name.lowercase() } // Sort products with same position by name
        .sortedBy(Product::position)
)

private fun ProductDto.toModel() = Product(
    id = this.id,
    name = this.name,
    price = this.price.cent,
    soldOut = this.soldOut,
    allergens = this.allergens.map { allergen ->
        Allergen(allergen.id, allergen.name, allergen.shortName)
    },
    position = this.position,
)

private fun ProductGroupDto.toEntry(eventId: Long, timestamp: Instant) = ProductGroupEntry(
    id = this.id,
    name = this.name,
    eventId = eventId,
    position = this.position,
    products = this.products.map { it.toEntry() },
    updatedAt = timestamp
)

private fun ProductDto.toEntry() = ProductEntry(
    id = this.id,
    name = this.name,
    price = this.price,
    soldOut = this.soldOut,
    allergens = this.allergens.map {
        AllergenEntry(id = it.id, name = it.name, shortName = it.shortName)
    },
    position = this.position,
)

private fun ProductEntry.toModel() = Product(
    id = this.id!!,
    name = this.name!!,
    price = this.price!!.cent,
    soldOut = this.soldOut!!,
    allergens = this.allergens.map { Allergen(it.id!!, it.name!!, it.shortName!!) },
    position = this.position
)

private fun ProductGroupEntry.toModel() = ProductGroup(
    id = this.id!!,
    name = this.name!!,
    position = this.position,
    products = this.products.map(ProductEntry::toModel)
)
