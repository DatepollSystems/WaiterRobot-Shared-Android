package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.datepollsystems.waiterrobot.android.ui.core.CenteredText
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListEffect
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.noTableFound
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun TableListScreen(
    vm: TableListViewModel = getViewModel(),
    scaffoldState: ScaffoldState,
    navigator: NavController,
) {
    val state = vm.collectAsState().value

    vm.collectSideEffect { handleSideEffects(it, navigator) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(CommonApp.settings.eventName) },
            )
        }
    ) { contentPadding ->
        View(
            state = state,
            paddingValues = contentPadding,
            onRefresh = vm::loadTables
        ) {
            if (state.tables.isEmpty()) {
                CenteredText(text = L.tableList.noTableFound())
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    columns = GridCells.Adaptive(80.dp)
                ) {
                    items(state.tables, key = { it.id }) { table ->
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

private fun handleSideEffects(effect: TableListEffect, navigator: NavController) {
    when (effect) {
        is TableListEffect.Navigate -> navigator.handleNavAction(effect.action)
    }
}
