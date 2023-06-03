package org.datepollsystems.waiterrobot.shared.features.order.repository

import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.order.api.ProductApi
import org.datepollsystems.waiterrobot.shared.features.order.api.models.ProductDto
import org.datepollsystems.waiterrobot.shared.features.order.db.ProductDatabase
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductEntry
import org.datepollsystems.waiterrobot.shared.features.order.models.Allergen
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroup
import org.datepollsystems.waiterrobot.shared.features.order.models.ProductGroupWithProducts
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
        return productDb.getById(id)?.toModel()
            ?: getProductGroups(true) // TODO limit force update
                .flatMap(ProductGroupWithProducts::products)
                .find { it.id == id }
    }

    suspend fun getProductGroups(forceUpdate: Boolean = false): List<ProductGroupWithProducts> {
        val eventId = CommonApp.settings.selectedEventId

        fun <T : Any> Map<ProductGroup, List<T>>.mapProductGroupWithProducts(
            mapper: (T) -> Product
        ): List<ProductGroupWithProducts> {
            return this.map { (group, products) ->
                ProductGroupWithProducts(
                    group = group,
                    products = products.map(mapper)
                        .sortedBy { it.name.lowercase() }
                        .sortedBy(Product::soldOut)
                )
            }.sortedBy { it.group.name }
        }

        fun loadFromDb(): List<ProductGroupWithProducts>? {
            logger.i { "Fetching products from DB ..." }
            val dbProducts = productDb.getForEvent(eventId)
            logger.d { "Found ${dbProducts.count()} products in DB" }

            return if (dbProducts.isEmpty() || dbProducts.any { it.updated.olderThan(maxAge) }) {
                null
            } else {
                dbProducts.groupBy { it.productGroup!!.toModel() }
                    .mapProductGroupWithProducts(ProductEntry::toModel)
            }
        }

        suspend fun loadFromApiAndStore(): List<ProductGroupWithProducts> {
            logger.i { "Loading products from api ..." }

            val timestamp = Now()
            val apiProducts = productApi.getProducts(eventId)
            logger.d { "Got ${apiProducts.sumOf { it.products.count() }} products from api" }

            val modelGroups = apiProducts.associate { it.id to ProductGroup(it.id, it.name) }
            val entryGroups =
                apiProducts.associate { it.id to ProductEntry.ProductGroup(it.id, it.name) }

            logger.i { "Remove old products from DB ..." }
            productDb.deleteForEvent(eventId)

            logger.i { "Saving products to DB ..." }
            productDb.insert(apiProducts.flatMap { group ->
                group.products.map { it.toEntry(eventId, entryGroups[group.id]!!, timestamp) }
            })

            return apiProducts.associate { group ->
                modelGroups[group.id]!! to group.products
            }.mapProductGroupWithProducts(ProductDto::toModel)
        }

        return if (forceUpdate) {
            loadFromApiAndStore()
        } else {
            loadFromDb() ?: loadFromApiAndStore()
        }
    }

    companion object {
        private val maxAge = 24.hours
    }
}

private fun ProductDto.toModel() = Product(
    id = this.id,
    name = this.name,
    price = this.price.cent,
    soldOut = this.soldOut,
    allergens = this.allergens.map { allergen ->
        Allergen(allergen.id, allergen.name, allergen.shortName)
    },
)

private fun ProductDto.toEntry(
    eventId: Long,
    group: ProductEntry.ProductGroup,
    timestamp: Instant
) = ProductEntry(
    id = this.id,
    eventId = eventId,
    name = this.name,
    price = this.price,
    soldOut = this.soldOut,
    productGroup = group,
    allergens = this.allergens.map {
        ProductEntry.Allergen(id = it.id, name = it.name, shortName = it.shortName)
    }.toRealmList(),
    updatedAt = timestamp
)

private fun ProductEntry.toModel() = Product(
    id = this.id!!,
    name = this.name!!,
    price = this.price!!.cent,
    soldOut = this.soldOut!!,
    allergens = this.allergens!!.map { Allergen(it.id!!, it.name!!, it.shortName!!) },
)

private fun ProductEntry.ProductGroup.toModel() = ProductGroup(
    id = this.id!!,
    name = this.name!!
)
