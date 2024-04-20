package org.datepollsystems.waiterrobot.shared.features.billing.viewmodel

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestV1Dto
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
        val items = billingRepository.getBillForTable(table).associateBy { it.productId }
        reduce { state.copy(_billItems = items, viewState = ViewState.Idle) }
    }

    fun paySelection() = intent {
        reduce { state.withViewState(viewState = ViewState.Loading) }
        billingRepository.payBill(table, state.billItems.filter { it.selectedForBill > 0 })

        loadBill()

        reduce {
            state.copy(change = null, moneyGivenText = "")
        }
    }

    fun initiateContactLessPayment() = intent {
        // TODO we also ned to verify that the phone was able to connect to the reader

        val stripeProvider = CommonApp.stripeProvider
        if (stripeProvider == null) {
            logger.e("Tried to initiate contactless payment but no stripe provider was set.")
            return@intent
        }

        reduce { state.withViewState(viewState = ViewState.Loading) }
        val paymentIntent = stripeApi.createPaymentIntent(
            PayBillRequestV1Dto(
                tableId = table.id,
                state.billItems.mapNotNull {
                    if (it.selectedForBill <= 0) {
                        null
                    } else {
                        PayBillRequestV1Dto.BillItemDto(it.productId, it.selectedForBill)
                    }
                }
            )
        )

        stripeProvider.initiatePayment(paymentIntent)

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

    fun addItem(id: Long, amount: Int) = intent {
        reduce {
            val item = state._billItems[id]

            if (item == null) {
                logger.e("Tried to add product with id '$id' but could not find the product on the bill.")
                return@reduce state
            }

            val newAmount = (item.selectedForBill + amount).coerceIn(0..item.ordered)

            val newItem = item.copy(selectedForBill = newAmount)
            val newBill = state._billItems.plus(newItem.productId to newItem)
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
