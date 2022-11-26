package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.noTableFound
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun TableListScreen(vm: TableListViewModel = getViewModel()) {
    val state = vm.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TableList" /* TODO */) },
            )
        }
    ) { contentPadding ->
        View(
            state = state,
            paddingValues = contentPadding,
            onRefresh = vm::loadTables
        ) {
            if (state.tables.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()), // Body of View must be scrollable
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = L.tableList.noTableFound(),
                        textAlign = TextAlign.Center
                    )
                }
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
                            onClick = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}
