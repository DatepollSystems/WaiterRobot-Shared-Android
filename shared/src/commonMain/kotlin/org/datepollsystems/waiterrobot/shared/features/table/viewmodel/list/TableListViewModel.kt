package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class TableListViewModel internal constructor(
    private val tableRepository: TableRepository
) : AbstractViewModel<TableListState, TableListEffect>(TableListState()) {

    override fun onCreate(state: TableListState) {
        loadTables()
    }

    fun loadTables(forceUpdate: Boolean = false) = intent {
        logger.d { "Load tables ..." }
        reduce { state.copy(viewState = ViewState.Loading) }

        val tables = tableRepository.getTables(forceUpdate)

        reduce { state.copy(viewState = ViewState.Idle, tables = tables) }
    }

    fun onTableClick(table: Table) = intent {
        navigator.push(Screen.TableDetailScreen(table))
    }

    fun openSettings() = intent {
        navigator.push(Screen.SettingsScreen)
    }

    override fun update() = loadTables(true)
}
