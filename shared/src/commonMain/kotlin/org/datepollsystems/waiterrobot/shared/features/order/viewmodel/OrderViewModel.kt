package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import org.datepollsystems.waiterrobot.shared.core.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.features.order.repository.OrderRepository
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail.TableDetailViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.*
import org.datepollsystems.waiterrobot.shared.utils.extensions.emptyToNull
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

@Suppress("TooManyFunctions")
class OrderViewModel internal constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val table: Table,
    private val initialItemId: Long?
) : AbstractViewModel<OrderState, OrderEffect>(OrderState()) {

    override fun onCreate(state: OrderState) {
        if (initialItemId == null) {
            intent {
                reduce { state.withViewState(ViewState.Loading) }
                val allProducts = productRepository.getProductGroups()
                reduce { state.copy(productGroups = allProducts, viewState = ViewState.Idle) }
            }
        } else {
            addItem(initialItemId, 1)
        }
    }

    fun addItem(id: Long, amount: Int) =
        addItem(id, amount) { productRepository.getProductById(id) }

    fun addItem(product: Product, amount: Int) =
        addItem(product.id, amount) { productRepository.getProductById(product.id) }

    fun addItemNote(item: OrderItem, note: String?) = intent {
        @Suppress("NAME_SHADOWING")
        val note = note?.trim().emptyToNull()

        // Adding a note to non existing orderItem is not possible
        val newItem = item.copy(note = note)
        val newOrder = state._currentOrder.plus(newItem.product.id to newItem)

        reduce { state.copy(_currentOrder = newOrder) }
    }

    fun sendOrder() = intent {
        reduce { state.withViewState(ViewState.Loading) }

        val order = state.currentOrder
        try {
            orderRepository.sendOrder(table, order)

            updateParent<TableDetailViewModel>()

            reduce { state.copy(viewState = ViewState.Idle, _currentOrder = emptyMap()) }
            navigator.popUpTo(Screen.TableDetailScreen(table), inclusive = false)
        } catch (e: ApiException.ProductSoldOut) {
            val soldOutProduct = order.first { it.product.id == e.productId }.product
            reduceError(
                L.order.productSoldOut.title(),
                L.order.productSoldOut.descOrderSent(soldOutProduct.name)
            ) {
                removeAllOfProduct(soldOutProduct.id)
                dismissError()
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate") // used on iOS
    fun removeAllOfProduct(productId: Long) = intent {
        reduce { state.copy(_currentOrder = state._currentOrder.minus(productId)) }
    }

    fun goBack() = intent {
        if (state._currentOrder.isEmpty()) {
            navigator.pop()
        } else {
            reduce { state.copy(showConfirmationDialog = true) }
        }
    }

    fun abortOrder() = intent {
        // Hide the confirmation dialog before navigation away,
        // as otherwise on iOS it would be still shown on the new screen
        reduce { state.copy(showConfirmationDialog = false) }
        navigator.pop()
    }

    fun keepOrder() = intent {
        reduce { state.copy(showConfirmationDialog = false) }
    }

    private fun addItem(id: Long, amount: Int, fallback: suspend () -> Product?) = intent {
        val item = state._currentOrder[id] ?: run {
            val item = fallback() ?: return@run null
            if (item.soldOut) {
                logger.w("Tried to add product (id: $id) which is already sold out.")
                reduceError(
                    L.order.productSoldOut.title(),
                    L.order.productSoldOut.descOrderAdd(item.name)
                )
                return@intent
            }
            return@run item.toNewOrderItem()
        }

        if (item == null) {
            logger.w("Tried to add product with id '$id' but could not find the product.")
            reduceError(L.order.couldNotFindProduct.title(), L.order.couldNotFindProduct.desc())
            return@intent
        }

        val newAmount = item.amount + amount

        val newOrder = if (newAmount <= 0) {
            state._currentOrder.minus(item.product.id)
        } else {
            val newItem = item.copy(amount = newAmount)
            state._currentOrder.plus(newItem.product.id to newItem)
        }

        // Reset products to clear filter
        val allProducts = productRepository.getProductGroups()
        reduce { state.copy(_currentOrder = newOrder, productGroups = allProducts) }
    }

    fun filterProducts(filter: String) = intent {
        // TODO do filter on db/repository layer?
        val allProducts = productRepository.getProductGroups()
        if (filter.isEmpty()) {
            reduce { state.copy(productGroups = allProducts) }
        } else {
            reduce {
                state.copy(
                    productGroups = allProducts.map { group ->
                        val filteredProducts = group.products
                            .filter { it.name.contains(filter, ignoreCase = true) }
                        // Also add groups with no products so that the tabs do not change
                        group.copy(products = filteredProducts)
                    },
                )
            }
        }
    }

    private fun Product.toNewOrderItem(): OrderItem {
        require(!soldOut) { "Product is sold out, not allowed to add to an Order" }
        return OrderItem(product = this, amount = 0, note = null)
    }
}
