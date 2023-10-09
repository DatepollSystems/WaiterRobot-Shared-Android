package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail

import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription

class TableDetailViewModel internal constructor(
    private val tableRepository: TableRepository,
    private val table: Table
) : AbstractViewModel<TableDetailState, TableDetailEffect>(TableDetailState()) {

    override suspend fun SimpleSyntax<TableDetailState, NavOrViewModelEffect<TableDetailEffect>>.onCreate() {
        repeatOnSubscription {
            loadOrder()
        }
    }

    // TODO can we somehow delegate this to the repository, so that we can call refresh of the orders
    //  from another viewModel which then emits a new value on the "getUnpaidItemsForTable" Flow.
    //  This way the AbstractViewModel.update mechanism would not be needed anymore.
    //  Or is there even a cleaner way? Or does the repeatOnSubscription already solve the issue?
    //  --> It is already handled by repeatOnSubscription
    fun refreshOrder() = intent { loadOrder() }

    private suspend fun SimpleSyntax<TableDetailState, NavOrViewModelEffect<TableDetailEffect>>.loadOrder() {
        tableRepository.getUnpaidItemsForTable(table).collect { resource ->
            reduce {
                state.copy(
                    orderedItemsResource = resource.map {
                        it ?: state.orderedItemsResource.data // Keep current state on error
                    }
                )
            }
        }
    }

    fun openOrderScreen(initialItemId: Long? = null) = intent {
        navigator.push(Screen.OrderScreen(table, initialItemId))
    }

    fun openBillingScreen() = intent {
        navigator.push(Screen.BillingScreen(table))
    }
}
