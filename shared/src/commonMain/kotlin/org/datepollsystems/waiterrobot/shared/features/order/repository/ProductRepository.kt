package org.datepollsystems.waiterrobot.shared.features.order.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.CachedRepository
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
import org.datepollsystems.waiterrobot.shared.utils.extensions.runCatchingCancelable
import kotlin.time.Duration.Companion.hours

internal class ProductRepository(
    private val productApi: ProductApi,
    private val productDb: ProductDatabase
) : CachedRepository<List<ProductGroupEntry>, List<ProductGroup>>() {

    override suspend fun onStart() {
        productDb.deleteOlderThan(maxAge)
    }

    suspend fun getProductById(id: Long): Product? {
        return productDb.getProductById(id)?.toModel()
            ?: runCatchingCancelable {
                update() // TODO limit updates?
                productDb.getProductById(id)?.toModel()
            }.getOrNull()
    }

    override fun query(): Flow<List<ProductGroupEntry>> =
        productDb.getForEventFlow(CommonApp.settings.selectedEventId)
            .map { groups ->
                // TODO move this to db query?
                groups.filter { it.products.isNotEmpty() } // Do not show groups that do not have products at all
                    .sortedBy { it.name.lowercase() } // Sort groups with same position by name
                    .sortedBy(ProductGroupEntry::position)
            }

    override suspend fun update() {
        val eventId = CommonApp.settings.selectedEventId
        logger.i { "Loading products from api ..." }

        val timestamp = Now()
        val apiProducts = productApi.getProducts(eventId)
        logger.d { "Got ${apiProducts.sumOf { it.products.count() }} products from api" }

        val entryGroups = apiProducts.map { it.toEntry(eventId, timestamp) }

        logger.i { "Replace products in DB ..." }
        productDb.replace(entryGroups)
    }

    override fun mapDbEntity(dbEntity: List<ProductGroupEntry>): List<ProductGroup> =
        dbEntity.map(ProductGroupEntry::toModel)

    override fun shouldFetch(cache: List<ProductGroupEntry>): Boolean =
        cache.isEmpty() || cache.any { it.updated.olderThan(maxAge) }

    companion object {
        private val maxAge = 24.hours
    }
}

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
    id = this.id,
    name = this.name,
    price = this.price.cent,
    soldOut = this.soldOut,
    allergens = this.allergens.map { Allergen(it.id, it.name, it.shortName) },
    position = this.position
)

private fun ProductGroupEntry.toModel() = ProductGroup(
    id = this.id,
    name = this.name,
    position = this.position,
    products = this.products.map(ProductEntry::toModel).sort()
)

private fun List<Product>.sort() = this
    .sortedBy { it.name.lowercase() } // Sort products with same position by name
    .sortedBy(Product::position)
