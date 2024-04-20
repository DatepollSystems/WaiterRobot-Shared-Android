package org.datepollsystems.waiterrobot.shared.features.billing.viewmodel

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.ChangeBreakUp.Companion.breakUp
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import org.datepollsystems.waiterrobot.shared.utils.Money
import org.datepollsystems.waiterrobot.shared.utils.cent
import org.datepollsystems.waiterrobot.shared.utils.euro
import org.datepollsystems.waiterrobot.shared.utils.sumOf
import kotlin.math.abs
import kotlin.native.HiddenFromObjC

data class BillingState(
    override val viewState: ViewState = ViewState.Idle,
    val moneyGivenText: String = "",
    val change: Change? = null,
    @Suppress("ConstructorParameterNaming")
    internal val _billItems: Map<Long, BillItem> = emptyMap()
) : ViewModelState() {
    @HiddenFromObjC
    val billItems: List<BillItem> by lazy { _billItems.values.toList() }

    @Suppress("unused") // iOS only
    val billItemsArray: Array<BillItem> by lazy { billItems.toTypedArray() }

    val priceSum: Money by lazy { _billItems.values.sumOf(BillItem::priceSum) }

    val hasSelectedItems: Boolean by lazy { _billItems.any { it.value.selectedForBill > 0 } }

    val contactLessState: ContactLessState by lazy {
        if (CommonApp.stripeProvider == null) return@lazy ContactLessState.DISABLED

        when (val stripeSettings = CommonApp.settings.selectedEvent?.stripeSettings) {
            null, Event.StripeSettings.Disabled -> ContactLessState.DISABLED
            is Event.StripeSettings.Enabled -> {
                if (stripeSettings.minAmount > priceSum.cents) {
                    ContactLessState.AMOUNT_TOO_LOW
                } else {
                    ContactLessState.ENABLED
                }
            }
        }
    }

    override fun withViewState(viewState: ViewState): BillingState = copy(viewState = viewState)

    data class Change(
        val amount: Money,
        val breakUp: List<ChangeBreakUp> = amount.breakUp(),
        val brokenDown: Boolean = false
    ) {
        @Suppress("unused") // iOS only
        val breakUpArray: Array<ChangeBreakUp> by lazy { breakUp.toTypedArray() }
    }

    enum class ContactLessState {
        DISABLED, ENABLED, AMOUNT_TOO_LOW
    }
}

data class ChangeBreakUp(
    val quantity: Int,
    val amount: Money
) {
    companion object {
        private val amountOrder = listOf(
            100.euro, 50.euro, 20.euro, 10.euro, 5.euro, 2.euro, 1.euro,
            50.cent, 20.cent, 10.cent, 5.cent, 2.cent, 1.cent
        )

        internal fun Money.breakUp(exclude: Money? = null): List<ChangeBreakUp> = buildList {
            amountOrder.filter { it != exclude }
                .fold(abs(this@breakUp.cents)) { leftOverCents, currentAmount ->
                    val quantity = leftOverCents / currentAmount.cents
                    if (quantity > 0) add(ChangeBreakUp(quantity, currentAmount))
                    leftOverCents - quantity * currentAmount.cents
                }
        }

        internal fun List<ChangeBreakUp>.breakDown(changeBreakUp: ChangeBreakUp): List<ChangeBreakUp> {
            if (changeBreakUp.amount <= amountOrder.last()) return this // Can not be further broken down

            return buildList {
                addAll(this@breakDown.filter { it.amount != changeBreakUp.amount })
                add(changeBreakUp.copy(quantity = changeBreakUp.quantity - 1))
                addAll(changeBreakUp.amount.breakUp(exclude = changeBreakUp.amount))
            }.groupBy { it.amount }
                .values
                .map { quantityList ->
                    quantityList.first().copy(
                        quantity = quantityList.sumOf(ChangeBreakUp::quantity)
                    )
                }
                .filter { it.quantity > 0 }
                .sortedByDescending { it.amount }
        }
    }
}
