package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.common.FloatingActionButton
import org.datepollsystems.waiterrobot.android.ui.core.CenteredText
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.BillingViewModel
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.closeAnyway
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.keepBill
import org.datepollsystems.waiterrobot.shared.generated.localization.noOpenBill
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.generated.localization.total
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
fun BillingScreen(
    table: Table,
    navigator: NavController,
    vm: BillingViewModel = getViewModel(parameters = { parametersOf(table) })
) {
    val state = vm.collectAsState().value
    vm.handleSideEffects(navigator)

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

    ScaffoldView(
        state = state,
        title = L.billing.title(table.number.toString(), table.groupName),
        topBarActions = {
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
    ) {
        Column {
            if (state.billItems.isEmpty()) {
                CenteredText(
                    text = L.billing.noOpenBill(table.number.toString(), table.groupName),
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
