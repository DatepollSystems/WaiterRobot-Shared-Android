package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class TableListViewModel internal constructor(
    private val tableRepository: TableRepository
) : AbstractViewModel<TableListState, TableListEffect>(TableListState()) {

    override suspend fun SimpleSyntax<TableListState, NavOrViewModelEffect<TableListEffect>>.onCreate() {
        coroutineScope {
            launch { watchRefresh() }
            refreshChannel.send(ForceUpdate(false))
        }
    }

    class ForceUpdate(val forceUpdate: Boolean)

    private val refreshChannel: Channel<ForceUpdate> = Channel(Channel.BUFFERED)

    fun loadTables(forceUpdate: Boolean = false) = intent {
        refreshChannel.send(ForceUpdate(forceUpdate))
    }

    fun toggleFilter(tableGroup: TableGroup) = intent {
        tableRepository.toggleGroupFilter(tableGroup)
    }

    fun clearFilter() = intent {
        tableRepository.clearFilter()
    }

    fun onTableClick(table: Table) = intent {
        navigator.push(Screen.TableDetailScreen(table))
    }

    fun openSettings() = intent {
        navigator.push(Screen.SettingsScreen)
    }

    private suspend fun SimpleSyntax<TableListState, NavOrViewModelEffect<TableListEffect>>.watchRefresh() {
        refreshChannel.receiveAsFlow().collectLatest {
            logger.d { "Load tables (forceUpdate=${it.forceUpdate}..." }
            tableRepository.getTableGroupsFlow(it.forceUpdate).collect {
                reduce { state.copy(tableGroups = it) }
            }
        }
    }

    override fun update() {
        loadTables(true)
    }
}
