package org.datepollsystems.waiterrobot.shared.features.billing.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.utils.sumOf

data class BillingState(
    override val viewState: ViewState = ViewState.Idle,
    val moneyGivenText: String = "",
    val changeText: String = "0.00 €",
    val showConfirmationDialog: Boolean = false,
    @Suppress("ConstructorParameterNaming")
    internal val _billItems: Map<Long, BillItem> = emptyMap()
) : ViewModelState() {
    val billItems: List<BillItem> by lazy { _billItems.values.toList() }

    val priceSum by lazy { _billItems.values.sumOf(BillItem::priceSum) }

    val hasSelectedItems by lazy { _billItems.any { it.value.selectedForBill > 0 } }

    override fun withViewState(viewState: ViewState): BillingState = copy(viewState = viewState)
}
