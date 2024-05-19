package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.IntentContext
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.datepollsystems.waiterrobot.shared.utils.repeatUntilCanceled
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import kotlin.time.Duration.Companion.minutes

class TableListViewModel internal constructor(
    private val tableRepository: TableRepository
) : AbstractViewModel<TableListState, TableListEffect>(TableListState()) {

    override suspend fun SimpleSyntax<TableListState, NavOrViewModelEffect<TableListEffect>>.onCreate() {
        coroutineScope {
            launch { tableRepository.listen() }
            launch {
                tableRepository.flow.collect {
                    reduce { state.copy(tableGroups = it) }
                }
            }
            launch { pollTablesWithOpenOrder() }
        }
    }

    @DefaultArgumentInterop.Enabled
    fun refreshTables() = intent {
        tableRepository.refresh()
    }

    fun toggleFilter(tableGroup: TableGroup) = intent {
        tableRepository.toggleGroupFilter(tableGroup)
    }

    fun showAll() = intent {
        tableRepository.showAll()
    }

    fun hideAll() = intent {
        tableRepository.hideAll()
    }

    fun onTableClick(table: Table) = intent {
        navigator.push(Screen.TableDetailScreen(table))
    }

    fun openSettings() = intent {
        navigator.push(Screen.SettingsScreen)
    }

    private suspend fun IntentContext<TableListState, TableListEffect>.pollTablesWithOpenOrder() {
        repeatOnSubscription {
            repeatUntilCanceled(1.minutes) {
                tableRepository.updateTablesWithOpenOrder()
            }
        }
    }

    override fun update() {
        refreshTables()
    }
}
