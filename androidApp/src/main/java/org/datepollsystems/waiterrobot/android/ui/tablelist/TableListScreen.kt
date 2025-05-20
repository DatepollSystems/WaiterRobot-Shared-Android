package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.LoadingView
import org.datepollsystems.waiterrobot.android.ui.core.view.RefreshableView
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.table.presentation.list.TableListViewModel
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
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
                    val tableGroups = state.tableGroups.data
                    if (tableGroups != null) {
                        BadgedBox(
                            badge = {
                                if (state.hasHiddenGroups) {
                                    Badge {
                                        Text(text = "!")
                                    }
                                }
                            }
                        ) {
                            IconButton(onClick = { showFilterSheet = true }) {
                                Icon(
                                    Icons.Filled.FilterList,
                                    contentDescription = "Filter table groups"
                                )
                            }
                        }
                    }
                    IconButton(onClick = vm::openSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
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
                    isDemoEvent = state.isDemoEvent,
                    refresh = vm::refreshTables
                )
            }

            if (showFilterSheet) {
                ModalBottomSheet(
                    sheetState = filterSheetState,
                    onDismissRequest = { showFilterSheet = false },
                ) {
                    TableGroupFilterSheet()
                }
            }
        }
    }
}
