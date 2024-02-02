package org.datepollsystems.waiterrobot.android.ui.order

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.common.FloatingActionButton
import org.datepollsystems.waiterrobot.android.ui.core.ConfirmDialog
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.viewmodel.OrderViewModel
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.addProduct
import org.datepollsystems.waiterrobot.shared.generated.localization.closeAnyway
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.keepOrder
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun OrderScreen(
    table: Table,
    initialItemId: Long? = null,
    navigator: NavController,
    vm: OrderViewModel = getViewModel(parameters = { parametersOf(table, initialItemId) })
) {
    val state = vm.collectAsState().value
    vm.handleSideEffects(navigator)

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var noteDialogItem: OrderItem? by remember { mutableStateOf(null) }

    val bottomSheetState = rememberModalBottomSheetState(
        // When opening the order screen waiter most likely wants to add a new product
        // -> show the product list immediately
        // But don't show it when the screen was opened with an initial item, this feels not nice
        initialValue = if (initialItemId == null) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    LaunchedEffect(bottomSheetState.targetValue) {
        if (bottomSheetState.targetValue == ModalBottomSheetValue.Hidden) {
            focusManager.clearFocus() // Close keyboard on sheet closing
        }
    }

    BackHandler {
        if (bottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
            // When Product search sheet is opened back press should only close it
            coroutineScope.launch { bottomSheetState.hide() }
        } else {
            vm.goBack()
        }
    }

    if (state.showConfirmationDialog) {
        ConfirmDialog(
            title = L.order.notSent.title(),
            text = L.order.notSent.desc(),
            confirmText = L.dialog.closeAnyway(),
            onConfirm = vm::abortOrder,
            dismissText = L.order.keepOrder(),
            onDismiss = vm::keepOrder
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

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),
        sheetContent = {
            ProductSearch(
                productGroups = state.productGroups,
                onSelect = {
                    vm.addItem(it, 1)
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                },
                onFilter = { vm.filterProducts(it) },
                close = { coroutineScope.launch { bottomSheetState.hide() } }
            )
        }
    ) {
        ScaffoldView(
            state = state,
            title = L.order.title(table.number.toString(), table.groupName),
            navigationIcon = {
                IconButton(onClick = vm::goBack) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            floatingActionButton = {
                Column {
                    if (state.currentOrder.isNotEmpty()) {
                        FloatingActionButton(
                            modifier = Modifier.scale(0.85f),
                            enabled = state.viewState == ViewState.Idle,
                            backgroundColor = MaterialTheme.colors.secondaryVariant,
                            onClick = vm::sendOrder
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = "Send Order")
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    FloatingActionButton(
                        onClick = { coroutineScope.launch { bottomSheetState.show() } }
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Product")
                    }
                }
            }
        ) {
            if (state.currentOrder.isEmpty()) {
                CenteredText(text = L.order.addProduct(), scrollAble = false)
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
