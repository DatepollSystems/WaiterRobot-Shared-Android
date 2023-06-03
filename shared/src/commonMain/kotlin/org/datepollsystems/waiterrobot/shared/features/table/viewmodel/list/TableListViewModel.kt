package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroupWithTables
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class TableListViewModel internal constructor(
    private val tableRepository: TableRepository
) : AbstractViewModel<TableListState, TableListEffect>(TableListState()) {

    override fun onCreate(state: TableListState) {
        loadTables()
    }

    fun loadTables(forceUpdate: Boolean = false) = loadTables(forceUpdate, showLoading = true)

    private fun loadTables(forceUpdate: Boolean, showLoading: Boolean) = intent {
        logger.d { "Load tables ..." }
        if (showLoading) reduce { state.copy(viewState = ViewState.Loading) }

        val tableGroups = tableRepository.getTableGroups(forceUpdate)
        val groups: Set<TableGroup> = tableGroups.mapTo(mutableSetOf()) { it.group }

        reduce {
            state.copy(
                viewState = ViewState.Idle,
                unselectedTableGroups = groups.minus(state.selectedTableGroups),
                filteredTableGroups = tableGroups.filterGroups(state.selectedTableGroups)
            )
        }
    }

    fun toggleFilter(tableGroup: TableGroup) = intent {
        reduce {
            if (state.selectedTableGroups.contains(tableGroup)) {
                state.copy(
                    selectedTableGroups = state.selectedTableGroups.minus(tableGroup),
                    unselectedTableGroups = state.unselectedTableGroups.plus(tableGroup),
                )
            } else {
                state.copy(
                    selectedTableGroups = state.selectedTableGroups.plus(tableGroup),
                    unselectedTableGroups = state.selectedTableGroups.minus(tableGroup),
                )
            }
        }

        loadTables(forceUpdate = false, showLoading = false)
    }

    fun clearFilter() = intent {
        reduce {
            state.copy(
                selectedTableGroups = emptySet(),
                unselectedTableGroups = state.unselectedTableGroups.plus(state.selectedTableGroups)
            )
        }

        loadTables(forceUpdate = false, showLoading = false)
    }

    fun onTableClick(table: Table) = intent {
        navigator.push(Screen.TableDetailScreen(table))
    }

    fun openSettings() = intent {
        navigator.push(Screen.SettingsScreen)
    }

    override fun update() = loadTables(true)

    private fun List<TableGroupWithTables>.filterGroups(selectedGroups: Set<TableGroup>): List<TableGroupWithTables> {
        return if (selectedGroups.isEmpty()) {
            this
        } else {
            this.filter { it.group in selectedGroups }
        }
    }
}
