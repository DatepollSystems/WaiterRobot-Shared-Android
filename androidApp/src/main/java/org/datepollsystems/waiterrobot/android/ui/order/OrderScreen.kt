package org.datepollsystems.waiterrobot.android.ui.order

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.core.ConfirmDialog
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.android.ui.core.view.ViewStateOverlay
import org.datepollsystems.waiterrobot.android.ui.product.ProductListScreen
import org.datepollsystems.waiterrobot.shared.features.order.domain.model.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.viewmodel.OrderViewModel
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.Table
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
fun OrderScreen(
    table: Table,
    initialItemId: Long? = null,
    navigator: NavController,
    vm: OrderViewModel = koinViewModel { parametersOf(table, initialItemId) }
) {
    val state by vm.collectAsState()
    vm.handleSideEffects(navigator)

    val coroutineScope = rememberCoroutineScope()
    var noteDialogItem: OrderItem? by remember { mutableStateOf(null) }
    var showConfirmGoBack: Boolean by remember { mutableStateOf(false) }

    // When opening the order screen waiter most likely wants to add a new product
    // -> show the product list immediately
    // But don't show it when the screen was opened with an initial item, this feels not nice
    var showProductSheet by remember { mutableStateOf(initialItemId == null) }
    val productSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val goBack: () -> Unit by rememberUpdatedState {
        when {
            state.currentOrder.isNotEmpty() -> showConfirmGoBack = true
            else -> vm.abortOrder()
        }
    }

    BackHandler(onBack = goBack)

    if (showConfirmGoBack) {
        ConfirmDialog(
            title = MR.strings.order_notSent_title.desc(),
            text = MR.strings.billing_notSent_desc.desc(),
            confirmText = MR.strings.dialog_closeAnyway.desc(),
            onConfirm = vm::abortOrder,
            cancelText = MR.strings.order_keepOrder.desc(),
            onCancel = { showConfirmGoBack = false }
        )
    }

    noteDialogItem?.let { item ->
        AddNoteDialog(
            item = item,
            onDismiss = { noteDialogItem = null },
            onSave = {
                vm.addItemNote(item, it)
                noteDialogItem = null
            }
        )
    }

    ScaffoldView(
        title = MR.strings.order_title(table.groupName, table.number),
        navigationIcon = {
            IconButton(onClick = goBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                if (state.currentOrder.isNotEmpty()) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        onClick = vm::sendOrder
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Send Order")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
                ExtendedFloatingActionButton(
                    onClick = { showProductSheet = true },
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = MR.strings.order_product_add()
                        )
                    },
                    text = { Text(MR.strings.order_product_add()) }
                )
            }
        },
        bottomSheet = {
            if (showProductSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showProductSheet = false },
                    sheetState = productSheetState,
                    dragHandle = null,
                    contentWindowInsets = { WindowInsets.statusBars }
                ) {
                    ProductListScreen(
                        onSelect = {
                            vm.addItem(it, 1)
                            coroutineScope.launch { productSheetState.hide() }
                                .invokeOnCompletion { showProductSheet = false }
                        },
                        close = {
                            when {
                                state.currentOrder.isEmpty() -> vm.abortOrder()
                                else -> {
                                    coroutineScope.launch { productSheetState.hide() }
                                        .invokeOnCompletion { showProductSheet = false }
                                }
                            }
                        },
                    )
                }
            }
        }
    ) { padding ->
        ViewStateOverlay(
            modifier = Modifier.padding(padding),
            state = state.orderingState
        ) {
            if (state.currentOrder.isEmpty()) {
                CenteredText(
                    modifier = Modifier.weight(1f),
                    text = MR.strings.order_product_add_desc(),
                    scrollAble = false
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.currentOrder, key = { it.product.id }) { orderItem ->
                        OrderListItem(
                            id = orderItem.product.id,
                            name = orderItem.product.name,
                            amount = orderItem.amount,
                            note = orderItem.note,
                            addAction = vm::addItem,
                            onLongClick = { noteDialogItem = orderItem }
                        )
                    }
                }
            }
        }
    }
}
