package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.datepollsystems.waiterrobot.android.ui.core.CenteredText
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.noTableFound
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun TableListScreen(
    vm: TableListViewModel = getViewModel(),
    navigator: NavController,
) {
    val state = vm.collectAsState().value
    vm.handleSideEffects(navigator)

    ScaffoldView(
        state = state,
        title = CommonApp.settings.eventName,
        topBarActions = {
            IconButton(onClick = vm::openSettings) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        },
        onRefresh = { vm.loadTables(forceUpdate = true) }
    ) {
        Column {
            if (state.selectedTableGroups.size + state.unselectedTableGroups.size > 1) {
                TableGroupFilter(
                    selectedGroups = state.selectedTableGroups,
                    unselectedGroups = state.unselectedTableGroups,
                    onToggle = vm::toggleFilter,
                    clearFilter = vm::clearFilter
                )
            }
            if (state.filteredTables.isEmpty()) {
                CenteredText(text = L.tableList.noTableFound(), scrollAble = true)
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    columns = GridCells.Adaptive(80.dp)
                ) {
                    items(state.filteredTables, key = { it.id }) { table ->
                        Table(
                            table = table,
                            onClick = { vm.onTableClick(table) }
                        )
                    }
                }
            }
        }
    }
}
