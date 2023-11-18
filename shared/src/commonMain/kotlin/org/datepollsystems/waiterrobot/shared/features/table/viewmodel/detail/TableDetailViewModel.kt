package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail

import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.datepollsystems.waiterrobot.shared.utils.repeatUntilCanceled
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import kotlin.time.Duration.Companion.minutes

class TableDetailViewModel internal constructor(
    private val tableRepository: TableRepository,
    private val table: Table
) : AbstractViewModel<TableDetailState, TableDetailEffect>(TableDetailState()) {

    override suspend fun SimpleSyntax<TableDetailState, NavOrViewModelEffect<TableDetailEffect>>.onCreate() {
        repeatOnSubscription {
            repeatUntilCanceled(1.minutes) { loadOrder() }
        }
    }

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
