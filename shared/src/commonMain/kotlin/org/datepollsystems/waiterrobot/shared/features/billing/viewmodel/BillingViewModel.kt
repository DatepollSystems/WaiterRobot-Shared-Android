package org.datepollsystems.waiterrobot.shared.features.billing.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.billing.repository.BillingRepository
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail.TableDetailViewModel
import org.datepollsystems.waiterrobot.shared.utils.euro
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import kotlin.math.max
import kotlin.math.min

class BillingViewModel internal constructor(
    private val billingRepository: BillingRepository,
    private val table: Table
) : AbstractViewModel<BillingState, BillingEffect>(BillingState()) {

    override fun onCreate(state: BillingState) {
        loadBill()
    }

    fun loadBill() = intent {
        reduce { state.withViewState(viewState = ViewState.Loading) }
        val items = billingRepository.getBillForTable(table).associateBy { it.productId }
        reduce { state.copy(_billItems = items, viewState = ViewState.Idle) }
    }

    fun paySelection() = intent {
        reduce { state.withViewState(viewState = ViewState.Loading) }
        billingRepository.payBill(table, state.billItems.filter { it.selectedForBill > 0 })

        updateParent<TableDetailViewModel>()
        loadBill()

        reduce { state.copy(changeText = "0", moneyGivenText = "") }
    }

    fun moneyGiven(moneyGiven: String) = intent {
        if (!moneyGiven.matches(Regex("""^(\d+([.,]\d{0,2})?)?$"""))) return@intent
        val givenText = moneyGiven.replace(",", ".")
        reduce { state.copy(moneyGivenText = givenText) }
        try {
            val given = givenText.euro
            reduce { state.copy(changeText = (given - state.priceSum).toString()) }
        } catch (e: Exception) {
            reduce { state.copy(changeText = "NaN") }
        }
    }

    fun addItem(id: Long, amount: Int) = intent {
        val item = state._billItems[id]

        if (item == null) {
            logger.e("Tried to add product with id '$id' but could not find the product on the bill.")
            return@intent
        }

        val newAmount = max(0, min(item.selectedForBill + amount, item.ordered))

        val newItem = item.copy(selectedForBill = newAmount)
        val newBill = state._billItems.plus(newItem.productId to newItem)

        reduce { state.copy(_billItems = newBill) }
    }

    fun selectAll() = intent {
        val newBill =
            state._billItems.mapValues { it.value.copy(selectedForBill = it.value.ordered) }

        reduce { state.copy(_billItems = newBill) }
    }

    fun unselectAll() = intent {
        val newBill = state._billItems.mapValues { it.value.copy(selectedForBill = 0) }

        reduce { state.copy(_billItems = newBill) }
    }

    fun goBack() = intent {
        if (state.hasSelectedItems) {
            reduce { state.copy(showConfirmationDialog = true) }
        } else {
            navigator.pop()
        }
    }

    fun abortBill() = intent {
        // Hide the confirmation dialog before navigation away, as otherwise on iOS it would be still shown on the new screen
        reduce { state.copy(showConfirmationDialog = false) }
        navigator.pop()
    }

    fun keepBill() = intent {
        reduce { state.copy(showConfirmationDialog = false) }
    }
}
