package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail

import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class TableDetailViewModel internal constructor(
    private val tableRepository: TableRepository,
    private val table: Table
) : AbstractViewModel<TableDetailState, TableDetailEffect>(TableDetailState()) {

    override fun onCreate(state: TableDetailState) {
        loadOrder()
    }

    fun loadOrder() = intent {
        reduce { state.withViewState(ViewState.Loading) }

        val items = tableRepository.getUnpaidItemsForTable(table)

        reduce { state.copy(orderedItems = items, viewState = ViewState.Idle) }
    }

    override fun update() = loadOrder()

    fun openOrderScreen(initialItemId: Long? = null) = intent {
        navigator.push(Screen.OrderScreen(table, initialItemId))
    }

    fun openBillingScreen() = intent {
        navigator.push(Screen.BillingScreen(table))
    }
}
