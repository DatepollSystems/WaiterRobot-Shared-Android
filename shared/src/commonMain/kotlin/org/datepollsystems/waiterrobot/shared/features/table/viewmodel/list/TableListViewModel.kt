package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import kotlinx.coroutines.delay
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class TableListViewModel internal constructor() :
    AbstractViewModel<TableListState, TableListEffect>(TableListState()) {

    override fun onCreate(state: TableListState) {
        loadTables()
    }

    fun loadTables() = intent {
        logger.d { "Load tables ..." }
        reduce { state.copy(viewState = ViewState.Loading) }

        // TODO get from API/CacheDB
        val tables = (0..10L).map { Table(it, it + 1) }
        delay(1000)

        reduce { state.copy(viewState = ViewState.Idle, tables = tables) }
    }

    override fun update() = loadTables()
}
