package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.common.FloatingActionButton
import org.datepollsystems.waiterrobot.android.ui.core.CenteredText
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.BillingEffect
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.BillingViewModel
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.generated.localization.*
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
@Destination
fun BillingScreen(
    table: Table,
    navigator: NavController,
    vm: BillingViewModel = getViewModel(parameters = { parametersOf(table) })
) {
    val state = vm.collectAsState().value
    vm.collectSideEffect { handleSideEffects(it, navigator) }

    var showPayDialog by remember { mutableStateOf(false) }

    BackHandler(onBack = vm::goBack)

    if (state.showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = vm::keepBill,
            confirmButton = {
                TextButton(onClick = vm::abortBill) {
                    Text(text = L.dialog.closeAnyway())
                }
            },
            dismissButton = {
                Button(onClick = vm::keepBill) {
                    Text(L.billing.keepBill())
                }
            },
            title = {
                Text(text = L.billing.notSent.title())
            },
            text = {
                Text(text = L.billing.notSent.desc())
            }
        )
    }

    if (showPayDialog) {
        PayBillDialog(
            priceSum = state.priceSum.toString(),
            changeText = state.changeText,
            moneyGivenText = state.moneyGivenText,
            dismiss = { showPayDialog = false },
            onMoneyGivenChange = vm::moneyGiven,
            pay = vm::paySelection
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = L.billing.title(table.number.toString())) },
                actions = {
                    IconButton(onClick = vm::selectAll) {
                        Icon(Icons.Filled.DoneAll, contentDescription = "Select all items")
                    }
                    IconButton(onClick = vm::unselectAll) {
                        Icon(Icons.Filled.RemoveDone, contentDescription = "Unselect all items")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = vm::goBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape
            ) {
                Text(
                    text = L.billing.total() + ":",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = state.priceSum.toString(),
                    style = MaterialTheme.typography.h6
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                enabled = state.viewState == ViewState.Idle && state.hasSelectedItems,
                onClick = {
                    showPayDialog = true
                }
            ) {
                Icon(Icons.Filled.AttachMoney, contentDescription = "Cash")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { contentPadding ->
        View(
            modifier = Modifier.padding(contentPadding),
            state = state,
            onRefresh = vm::loadBill
        ) {
            Column {
                if (state.billItems.isEmpty()) {
                    CenteredText(
                        text = L.billing.noOpenBill(table.number.toString()),
                        scrollAble = true
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.billItems, key = BillItem::productId) { billItem ->
                            BillListItem(
                                item = billItem,
                                addAction = vm::addItem
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun handleSideEffects(effect: BillingEffect, navigator: NavController) {
    when (effect) {
        is BillingEffect.Navigate -> navigator.handleNavAction(effect.action)
    }
}
