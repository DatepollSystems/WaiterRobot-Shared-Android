package org.datepollsystems.waiterrobot.shared.features.billing.viewmodel

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestDto
import org.datepollsystems.waiterrobot.shared.features.billing.repository.BillingRepository
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.ChangeBreakUp.Companion.breakDown
import org.datepollsystems.waiterrobot.shared.features.stripe.api.StripeApi
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.utils.euro
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class BillingViewModel internal constructor(
    private val billingRepository: BillingRepository,
    private val stripeApi: StripeApi,
    private val table: Table
) : AbstractViewModel<BillingState, BillingEffect>(BillingState()) {

    override suspend fun SimpleSyntax<BillingState, NavOrViewModelEffect<BillingEffect>>.onCreate() {
        loadBill()
    }

    private fun loadBill() = intent {
        reduce { state.withViewState(viewState = ViewState.Loading) }
        val items = billingRepository.getBillForTable(table).associateBy { it.virtualId }
        reduce { state.copy(_billItems = items, viewState = ViewState.Idle) }
    }

    fun paySelection() = intent {
        reduce { state.withViewState(viewState = ViewState.Loading) }

        val newBillItems = billingRepository.payBill(
            table = table,
            items = state.billItems.filter { it.selectedForBill > 0 }
        )

        reduce {
            state.copy(
                viewState = ViewState.Idle,
                _billItems = newBillItems.associateBy { it.virtualId },
                change = null,
                moneyGivenText = ""
            )
        }
    }

    fun initiateContactLessPayment() = intent {
        val stripeProvider = CommonApp.stripeProvider
        if (stripeProvider == null) {
            logger.e("Tried to initiate contactless payment but no stripe provider was set.")
            return@intent
        }

        if (!stripeProvider.connectedToReader.value) {
            logger.e("Tried to initiate contactless payment but no reader was connected.")
            return@intent
        }

        reduce { state.withViewState(viewState = ViewState.Loading) }

        val paymentIntent = stripeApi.createPaymentIntent(
            PayBillRequestDto(
                tableId = table.id,
                state.billItems.flatMap {
                    it.orderProductIds.take(it.selectedForBill)
                }
            )
        )

        runCatching {
            stripeProvider.initiatePayment(paymentIntent)
        }.onFailure {
            logger.e("Failed to initiate payment", it)
            stripeProvider.cancelPayment(paymentIntent)
        }

        loadBill()

        reduce {
            state.copy(change = null, moneyGivenText = "")
        }
    }

    @OptIn(OrbitExperimental::class)
    fun moneyGiven(moneyGiven: String) = blockingIntent {
        if (!moneyGiven.matches(Regex("""^(\d+([.,]\d{0,2})?)?$"""))) return@blockingIntent
        val givenText = moneyGiven.replace(",", ".")
        reduce {
            try {
                val given = givenText.euro
                state.copy(
                    moneyGivenText = givenText,
                    change = BillingState.Change(amount = given - state.priceSum),
                )
            } catch (_: Exception) {
                state.copy(
                    moneyGivenText = givenText,
                    change = null,
                )
            }
        }
    }

    fun breakDownChange(changeBreakUp: ChangeBreakUp) = intent {
        reduce {
            val change = state.change
            state.copy(
                change = change?.copy(
                    breakUp = change.breakUp.breakDown(changeBreakUp),
                    brokenDown = true
                )
            )
        }
    }

    fun resetChange() = intent {
        reduce {
            state.copy(
                change = state.change?.amount?.let { BillingState.Change(it) }
            )
        }
    }

    fun addItem(virtualId: Long, amount: Int) = intent {
        reduce {
            val item = state._billItems[virtualId]

            if (item == null) {
                logger.e("Tried to add product with id '$virtualId' but could not find the product on the bill.")
                return@reduce state
            }

            val newAmount = (item.selectedForBill + amount).coerceIn(0..item.ordered)

            val newItem = item.copy(selectedForBill = newAmount)
            val newBill = state._billItems.plus(newItem.virtualId to newItem)
            state.copy(
                _billItems = newBill,
                moneyGivenText = "",
                change = null
            )
        }
    }

    fun selectAll() = intent {
        reduce {
            val newBill = state._billItems.mapValues {
                it.value.copy(selectedForBill = it.value.ordered)
            }
            state.copy(
                _billItems = newBill,
                moneyGivenText = "",
                change = null
            )
        }
    }

    fun unselectAll() = intent {
        reduce {
            val newBill = state._billItems.mapValues { it.value.copy(selectedForBill = 0) }
            state.copy(
                _billItems = newBill,
                moneyGivenText = "",
                change = null
            )
        }
    }

    fun abortBill() = intent {
        navigator.pop()
    }
}
