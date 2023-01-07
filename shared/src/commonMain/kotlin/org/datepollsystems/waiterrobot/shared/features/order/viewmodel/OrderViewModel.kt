package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import org.datepollsystems.waiterrobot.shared.core.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.models.Product
import org.datepollsystems.waiterrobot.shared.features.order.repository.OrderRepository
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail.TableDetailViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.utils.extensions.emptyToNull
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class OrderViewModel internal constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val table: Table,
    private val initialItemId: Long?
) : AbstractViewModel<OrderState, OrderEffect>(OrderState()) {

    override fun onCreate(state: OrderState) {
        intent {
            reduce { state.withViewState(ViewState.Loading) }

            val allProducts = productRepository.getProducts(true)
            // TODO handle initial product is sold out
            val order = runCatching {
                initialItemId
                    ?.let {
                        productRepository.getProductById(it)?.toNewOrderItem()?.copy(amount = 1)
                    }
                    ?.let { mapOf(it.product.id to it) }
            }.getOrNull()

            reduce {
                state.copy(
                    _products = allProducts,
                    viewState = ViewState.Idle,
                    _currentOrder = order ?: emptyMap()
                )
            }
        }
    }

    fun addItem(id: Long, amount: Int) =
        addItem(id, amount) { productRepository.getProductById(id)?.toNewOrderItem() }

    fun addItem(product: Product, amount: Int) =
        addItem(product.id, amount) { product.toNewOrderItem() }

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
            postSideEffect(
                OrderEffect.Navigate(
                    NavAction.PopUpTo(Screen.TableDetailScreen(table), inclusive = false)
                )
            )
        } catch (e: ApiException.ProductSoldOut) {
            val soldOutProduct = order.first { it.product.id == e.productId }.product
            reduceError(
                L.order.productSoldOut.title(),
                L.order.productSoldOut.desc(soldOutProduct.name)
            ) {
                removeAllOfProduct(soldOutProduct.id)
                dismissError()
            }
        }
    }

    fun removeAllOfProduct(productId: Long) = intent {
        reduce { state.copy(_currentOrder = state._currentOrder.minus(productId)) }
    }

    fun goBack() = intent {
        if (state._currentOrder.isEmpty()) {
            postSideEffect(OrderEffect.Navigate(NavAction.Pop))
        } else {
            reduce { state.copy(showConfirmationDialog = true) }
        }
    }

    fun abortOrder() = intent {
        // Hide the confirmation dialog before navigation away, as otherwise on iOS it would be still shown on the new screen
        reduce { state.copy(showConfirmationDialog = false) }
        postSideEffect(OrderEffect.Navigate(NavAction.Pop))
    }

    fun keepOrder() = intent {
        reduce { state.copy(showConfirmationDialog = false) }
    }

    private fun addItem(id: Long, amount: Int, fallback: suspend () -> OrderItem?) = intent {
        val item = state._currentOrder[id] ?: fallback()

        if (item == null) {
            logger.e("Tried to add product with id '$id' but could not find the product.")
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
        val allProducts = productRepository.getProducts()
        reduce { state.copy(_currentOrder = newOrder, _products = allProducts) }
    }

    fun filterProducts(filter: String) = intent {
        // TODO do filter on db/repository layer?
        val allProducts = productRepository.getProducts()
        if (filter.isEmpty()) {
            reduce { state.copy(_products = allProducts) }
        } else {
            reduce {
                state.copy(
                    _products = allProducts.filter {
                        it.name.contains(filter, ignoreCase = true)
                    }
                )
            }
        }
    }

    private fun Product.toNewOrderItem(): OrderItem {
        require(!soldOut) { "Product is sold out, not allowed to add to an Order" }
        return OrderItem(product = this, amount = 0, note = null)
    }
}