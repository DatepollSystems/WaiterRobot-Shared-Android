package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.features.order.repository.OrderRepository
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.descOrderSent
import org.datepollsystems.waiterrobot.shared.utils.extensions.emptyToNull
import org.orbitmvi.orbit.syntax.simple.SimpleContext
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

@Suppress("TooManyFunctions")
class OrderViewModel internal constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val table: Table,
    private val initialItemId: Long?
) : AbstractViewModel<OrderState, OrderEffect>(OrderState()) {

    override suspend fun SimpleSyntax<OrderState, NavOrViewModelEffect<OrderEffect>>.onCreate() {
        coroutineScope {
            launch { productRepository.listen() }
            launch {
                productRepository.flow.collect { resource ->
                    reduce {
                        state.copy(
                            productGroups = if (state.filter.isEmpty()) {
                                resource
                            } else {
                                resource.map { allProductGroups ->
                                    allProductGroups?.map { group ->
                                        val filteredProducts = group.products.filter {
                                            it.name.contains(state.filter, ignoreCase = true)
                                        }
                                        // Keep groups with no products so that the tabs do not change
                                        group.copy(products = filteredProducts)
                                    }
                                }
                            }
                        )
                    }
                }
            }

            if (initialItemId != null) {
                addItem(initialItemId, 1)
            }
        }
    }

    fun addItem(product: Product, amount: Int) = addItem(product.id, amount)

    fun addItemNote(item: OrderItem, note: String?) = intent {
        @Suppress("NAME_SHADOWING")
        val note = note?.trim().emptyToNull()

        // Adding a note to non existing orderItem is not possible
        val newItem = item.copy(note = note)

        reduce {
            state.copy(
                _currentOrder = Resource.Success(
                    state._currentOrder.dataOrEmpty.plus(newItem.product.id to newItem)
                )
            )
        }
    }

    fun sendOrder() = intent {
        reduce {
            state.copy(
                _currentOrder = Resource.Loading(state._currentOrder.data ?: emptyMap())
            )
        }

        val order = state.currentOrder.data
        if (order == null) {
            reduce {
                state.copy(
                    _currentOrder = Resource.Error(
                        "Empty order", // TODO translate
                        state._currentOrder.data ?: emptyMap()
                    )
                )
            }
            return@intent
        }

        try {
            orderRepository.sendOrder(table, order)

            reduce { state.copy(_currentOrder = Resource.Success(emptyMap())) }
            navigator.popUpTo(Screen.TableDetailScreen(table), inclusive = false)
        } catch (e: ApiException.ProductSoldOut) {
            val soldOutProduct = order.first { it.product.id == e.productId }.product
            reduce { productSoldOut(soldOutProduct) }
        }
    }

    @Suppress("unused") // used on iOS
    fun removeAllOfProduct(productId: Long) = intent {
        reduce {
            state.copy(
                _currentOrder = Resource.Success(
                    state._currentOrder.dataOrEmpty.minus(productId)
                )
            )
        }
    }

    fun abortOrder() = intent {
        navigator.pop()
    }

    fun addItem(id: Long, amount: Int) = intent {
        val product = productRepository.getProductById(id)

        if (product == null) {
            logger.w("Tried to add product with id '$id' but could not find the product.")
            reduce {
                state.copy(
                    _currentOrder = Resource.Error(
                        L.order.couldNotFindProduct.desc(),
                        state._currentOrder.dataOrEmpty.minus(id)
                    )
                )
            }
            return@intent
        }

        if (product.soldOut) {
            logger.w("Tried to add product (id: $id) which is already sold out.")
            reduce { productSoldOut(product) }
            return@intent
        }

        reduce {
            val item = state._currentOrder.dataOrEmpty[id] ?: product.toNewOrderItem()
            val newAmount = item.amount + amount

            val newOrder = if (newAmount <= 0) {
                state._currentOrder.dataOrEmpty.minus(product.id)
            } else {
                val newItem = item.copy(amount = newAmount)
                state._currentOrder.dataOrEmpty.plus(newItem.product.id to newItem)
            }
            state.copy(
                _currentOrder = Resource.Success(newOrder),
                filter = ""
            )
        }
        productRepository.requery()
    }

    private fun SimpleContext<OrderState>.productSoldOut(product: Product): OrderState {
        return state.copy(
            _currentOrder = Resource.Error(
                L.order.productSoldOut.descOrderSent(product.name),
                state._currentOrder.dataOrEmpty.minus(product.id)
            )
        )
    }

    fun filterProducts(filter: String) = intent {
        reduce { state.copy(filter = filter) } // TODO can we move the filter to the repository?
        productRepository.requery()
    }

    fun refreshProducts() = intent {
        productRepository.refresh()
    }

    private fun Product.toNewOrderItem(): OrderItem {
        require(!soldOut) { "Product is sold out, not allowed to add to an Order" }
        return OrderItem(product = this, amount = 0, note = null)
    }

    private val Resource<Map<Long, OrderItem>>.dataOrEmpty get() = this.data ?: emptyMap()
}
