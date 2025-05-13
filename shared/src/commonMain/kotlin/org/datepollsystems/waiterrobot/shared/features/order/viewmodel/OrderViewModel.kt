package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format
import kotlinx.coroutines.coroutineScope
import org.datepollsystems.waiterrobot.shared.core.data.remote.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.DialogState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.data.OrderRepositoryImpl
import org.datepollsystems.waiterrobot.shared.features.order.domain.model.OrderItem
import org.datepollsystems.waiterrobot.shared.features.product.domain.GetProductUseCase
import org.datepollsystems.waiterrobot.shared.features.product.domain.RefreshProductGroupsUseCase
import org.datepollsystems.waiterrobot.shared.features.product.domain.model.Product
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.Table
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.extensions.emptyToNull
import org.datepollsystems.waiterrobot.shared.utils.randomUUID
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.subIntent

@OptIn(OrbitExperimental::class)
@Suppress("TooManyFunctions")
class OrderViewModel internal constructor(
    private val getProductUseCase: GetProductUseCase,
    private val refreshProductGroupsUseCase: RefreshProductGroupsUseCase,
    private val orderRepository: OrderRepositoryImpl,
    private val table: Table,
    private val initialItemId: Long?,
) : AbstractViewModel<OrderState, OrderEffect>(OrderState()) {

    private var currentOrderId = randomUUID()

    override suspend fun onCreate() = subIntent {
        coroutineScope {
            if (initialItemId != null) {
                addItem(initialItemId, 1)
            }
        }
    }

    fun addItem(product: Product, amount: Int) = addItem(product.id, amount)

    fun addItemNote(item: OrderItem, note: String?) = intent {
        val newItem = item.copy(note = note?.trim().emptyToNull())

        reduce {
            state.copy(
                _currentOrder = state._currentOrder.plus(newItem.product.id to newItem)
            )
        }
    }

    fun sendOrder() = intent {
        reduce { state.copy(orderingState = ViewState.Loading) }

        val order = state.currentOrder
        orderRepository.sendOrder(table, order, currentOrderId)
            .onSuccess {
                currentOrderId = randomUUID()
                navigator.popUpTo(Screen.TableDetailScreen(table), inclusive = false)
            }
            .onFailure { e ->
                when (e) {
                    is ApiException.ProductSoldOut -> {
                        val soldOutProduct = order.first { it.product.id == e.productId }.product
                        productSoldOut(soldOutProduct)
                    }

                    is ApiException.ProductStockToLow -> {
                        val stockToLowProduct = order.first { it.product.id == e.productId }.product
                        if (e.remaining <= 0) {
                            productSoldOut(stockToLowProduct)
                        } else {
                            stockToLow(stockToLowProduct, e.remaining)
                        }
                    }

                    is ApiException.NoLicence -> noLicence()

                    is ApiException.OrderAlreadySubmitted -> {
                        logger.w("Order was already submitted")
                        navigator.popUpTo(Screen.TableDetailScreen(table), inclusive = false)
                    }

                    else -> {
                        logger.e(e) { "Failed to send order" }
                        reduce {
                            state.copy(
                                orderingState = ViewState.Error(
                                    MR.strings.exceptions_title.desc(),
                                    MR.strings.exceptions_generic.desc(),
                                    onDismiss = ::dismissOrderError,
                                    primaryButton = DialogState.Button(
                                        MR.strings.dialog_ok.desc(),
                                        ::dismissOrderError,
                                    )
                                )
                            )
                        }
                    }
                }
            }
    }

    @Suppress("unused") // used on iOS
    fun removeAllOfProduct(productId: Long) = intent {
        reduce {
            state.copy(_currentOrder = state._currentOrder.minus(productId))
        }
    }

    fun abortOrder() = intent {
        navigator.pop()
    }

    fun addItem(id: Long, amount: Int) = intent {
        val product = getProductUseCase(id)

        if (product == null) {
            logger.w("Tried to add product with id '$id' but could not find the product.")
            reduce {
                state.copy(
                    orderingState = ViewState.Error(
                        title = MR.strings.order_product_not_found_title.desc(),
                        text = MR.strings.order_product_not_found_desc.desc(),
                        onDismiss = { removeItem(id) },
                        primaryButton = DialogState.Button(
                            text = MR.strings.dialog_ok.desc(),
                            action = { removeItem(id) }
                        )
                    )
                )
            }
            return@intent
        }

        if (product.soldOut) {
            logger.w("Tried to add product (id: $id) which is already sold out.")
            productSoldOut(product)
            return@intent
        }

        reduce {
            val item = state._currentOrder[id] ?: product.toNewOrderItem()
            val newAmount = item.amount + amount

            val newOrder = if (newAmount <= 0) {
                state._currentOrder.minus(product.id)
            } else {
                val newItem = item.copy(amount = newAmount)
                state._currentOrder.plus(newItem.product.id to newItem)
            }
            state.copy(_currentOrder = newOrder)
        }
    }

    private fun dismissOrderError() {
        intent {
            reduce { state.copy(orderingState = ViewState.Idle) }
        }
    }

    private fun removeItem(id: Long) = intent {
        reduce {
            state.copy(
                _currentOrder = state._currentOrder.minus(id),
                orderingState = ViewState.Idle
            )
        }
    }

    private suspend fun productSoldOut(product: Product) = subIntent {
        refreshProducts()
        reduce {
            state.copy(
                orderingState = ViewState.Error(
                    title = MR.strings.order_product_soldOut_title.desc(),
                    text = MR.strings.order_product_soldOut_desc_onSend.format(product.name),
                    onDismiss = { removeItem(product.id) },
                    primaryButton = DialogState.Button(
                        text = MR.strings.dialog_ok.desc(),
                        action = { removeItem(product.id) }
                    )
                )
            )
        }
    }

    private suspend fun stockToLow(product: Product, remaining: Int) = subIntent {
        refreshProducts()
        reduce {
            state.copy(
                orderingState = ViewState.Error(
                    title = MR.strings.order_product_stockToLow_title.desc(),
                    text = MR.strings.order_product_stockToLow_desc.format(remaining, product.name),
                    onDismiss = ::dismissOrderError,
                    primaryButton = DialogState.Button(
                        MR.strings.dialog_ok.desc(),
                        ::dismissOrderError
                    )
                )
            )
        }
    }

    private suspend fun noLicence() = subIntent {
        reduce {
            state.copy(
                orderingState = ViewState.Error(
                    title = MR.strings.order_noLicence_title.desc(),
                    text = MR.strings.order_noLicence_desc.desc(),
                    onDismiss = ::dismissOrderError,
                    primaryButton = DialogState.Button(
                        MR.strings.dialog_ok.desc(),
                        ::dismissOrderError
                    )
                )
            )
        }
    }

    fun refreshProducts() = intent {
        refreshProductGroupsUseCase()
    }

    private fun Product.toNewOrderItem(): OrderItem {
        require(!soldOut) { "Product is sold out, not allowed to add to an Order" }
        return OrderItem(product = this, amount = 0, note = null)
    }
}
