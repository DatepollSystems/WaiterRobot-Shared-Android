package org.datepollsystems.waiterrobot.android.ui.tabledetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.core.ErrorBar
import org.datepollsystems.waiterrobot.android.ui.core.LocalScaffoldState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.RefreshableView
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderedItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail.TableDetailViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.noOrder
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
fun TableDetailScreen(
    table: Table,
    vm: TableDetailViewModel = getViewModel { parametersOf(table) },
    navigator: NavController,
) {
    val state = vm.collectAsState().value

    vm.handleSideEffects(navigator)

    Scaffold(
        scaffoldState = LocalScaffoldState.current,
        topBar = {
            TopAppBar(
                title = { Text(L.tableDetail.title(table.number.toString(), table.groupName)) },
                navigationIcon = {
                    IconButton(onClick = navigator::popBackStack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            Column {
                if (!state.orderedItemsResource.data.isNullOrEmpty()) {
                    FloatingActionButton(
                        modifier = Modifier.scale(0.85f),
                        backgroundColor = MaterialTheme.colors.secondaryVariant,
                        onClick = vm::openBillingScreen
                    ) {
                        Icon(Icons.Filled.CreditCard, contentDescription = "Pay")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
                FloatingActionButton(onClick = vm::openOrderScreen) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Order")
                }
            }
        }
    ) {
        RefreshableView(
            modifier = Modifier.padding(it),
            loading = state.orderedItemsResource is Resource.Loading && state.orderedItemsResource.data != null,
            onRefresh = vm::refreshOrder,
        ) {
            if (state.orderedItemsResource is Resource.Loading && state.orderedItemsResource.data == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column {
                    val res = state.orderedItemsResource
                    val orderedItems = state.orderedItemsResource.data
                    if (orderedItems.isNullOrEmpty()) {
                        CenteredText(
                            modifier = Modifier.weight(1f),
                            text = L.tableDetail.noOrder(table.number.toString(), table.groupName),
                            scrollAble = true
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        ) {
                            items(orderedItems, key = OrderedItem::id) { item ->
                                OrderedItem(item = item) {
                                    vm.openOrderScreen(item.id)
                                }
                            }
                        }
                    }
                    // TODO retry action is covered by FAB...
                    if (res is Resource.Error) {
                        ErrorBar(exception = res.exception, retryAction = vm::refreshOrder)
                    }
                }
            }
        }
    }
}
