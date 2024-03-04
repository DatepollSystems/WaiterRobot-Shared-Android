package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.common.sectionHeader
import org.datepollsystems.waiterrobot.android.ui.core.ErrorBar
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.LoadingView
import org.datepollsystems.waiterrobot.android.ui.core.view.RefreshableView
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.noTableFound
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun TableListScreen(
    vm: TableListViewModel = koinViewModel(),
    navigator: NavController,
) {
    val state by vm.collectAsState()
    vm.handleSideEffects(navigator)

    val filterSheetState = rememberModalBottomSheetState()
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) },
        topBar = {
            TopAppBar(
                title = { Text(CommonApp.settings.eventName) },
                actions = {
                    IconButton(onClick = vm::openSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
        bottomBar = {
            val tableGroups = state.tableGroups.data
            if (!tableGroups.isNullOrEmpty()) {
                BottomAppBar(
                    actions = {
                        Box {
                            IconButton(onClick = { showFilterSheet = true }) {
                                Icon(
                                    Icons.Filled.FilterList,
                                    contentDescription = "Filter table groups"
                                )
                            }

                            if (tableGroups.any { it.hidden }) {
                                Badge(modifier = Modifier.align(Alignment.TopEnd)) {
                                    Text(text = "1")
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        RefreshableView(
            modifier = Modifier.padding(paddingValues),
            loading = state.tableGroups is Resource.Loading && state.tableGroups.data != null,
            onRefresh = vm::refreshTables,
        ) {
            if (state.tableGroups is Resource.Loading && state.tableGroups.data == null) {
                LoadingView()
            } else {
                TableGrid(
                    groupsResource = state.tableGroups,
                    onTableClick = vm::onTableClick,
                    refresh = vm::refreshTables
                )
            }

            if (showFilterSheet) {
                ModalBottomSheet(
                    sheetState = filterSheetState,
                    onDismissRequest = { showFilterSheet = false },
                ) {
                    TableGroupFilter(
                        tableGroups = state.tableGroups.data,
                        onToggle = vm::toggleFilter,
                        showAll = vm::showAll,
                        hideAll = vm::hideAll
                    )
                }
            }
        }
    }
}

@Composable
private fun TableGrid(
    groupsResource: Resource<List<TableGroup>>,
    onTableClick: (Table) -> Unit,
    refresh: () -> Unit
) {
    val tableGroups = groupsResource.data
    Column {
        if (groupsResource is Resource.Error) {
            ErrorBar(message = groupsResource.userMessage, retryAction = refresh)
        }

        if (tableGroups.isNullOrEmpty()) {
            CenteredText(
                modifier = Modifier.weight(1f),
                text = L.tableList.noTableFound(),
                scrollAble = true
            )
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
                tableGroups
                    .filterNot(TableGroup::hidden)
                    .forEach { group: TableGroup ->
                        // TODO use a sticky header
                        //  (Compose currently does not support sticky headers in LazyGrids)
                        sectionHeader(key = "group-${group.id}", title = group.name)
                        items(group.tables, key = Table::id) { table ->
                            Table(
                                table = table,
                                onClick = { onTableClick(table) }
                            )
                        }
                    }
            }
        }
    }
}
