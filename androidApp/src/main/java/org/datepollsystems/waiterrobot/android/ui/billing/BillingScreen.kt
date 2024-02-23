package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.ui.core.ConfirmDialog
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.BillingState
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.BillingViewModel
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.closeAnyway
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.keepBill
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.generated.localization.total
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun BillingScreen(
    table: Table,
    navigator: NavController,
    vm: BillingViewModel = koinViewModel { parametersOf(table) }
) {
    val state = vm.collectAsState().value
    vm.handleSideEffects(navigator)

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequest = remember { FocusRequester() }
    var showConfirmGoBack by remember { mutableStateOf(false) }
    val paymentSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    fun goBack(state: BillingState, paymentSheetState: ModalBottomSheetState) {
        when {
            paymentSheetState.targetValue != ModalBottomSheetValue.Hidden -> {
                coroutineScope.launch { paymentSheetState.hide() }
            }

            state.hasSelectedItems -> showConfirmGoBack = true
            else -> vm.abortBill()
        }
    }

    BackHandler(onBack = { goBack(state, paymentSheetState) })

    LaunchedEffect(paymentSheetState.isVisible) {
        if (paymentSheetState.isVisible) {
            focusRequest.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    if (showConfirmGoBack) {
        ConfirmDialog(
            title = L.billing.notSent.title(),
            text = L.billing.notSent.desc(),
            confirmText = L.dialog.closeAnyway(),
            onConfirm = vm::abortBill,
            cancelText = L.billing.keepBill(),
            onCancel = { showConfirmGoBack = false },
        )
    }

    ModalBottomSheetLayout(
        sheetContent = {
            PaymentView(
                sum = state.priceSum.toString(),
                moneyGivenText = state.moneyGivenText,
                moneyGiven = vm::moneyGiven,
                moneyGivenInputFocusRequester = focusRequest,
                change = state.change,
                breakDownChange = vm::breakDownChange,
                resetChangeBreakUp = vm::resetChange,
                onPayClick = {
                    vm.paySelection()
                    coroutineScope.launch {
                        focusManager.clearFocus()
                        paymentSheetState.hide()
                    }
                }
            )
        },
        sheetState = paymentSheetState,
        sheetShape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),
        sheetGesturesEnabled = false
    ) {
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
                IconButton(onClick = { goBack(state, paymentSheetState) }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            bottomBar = {
                BottomAppBar(contentPadding = PaddingValues(horizontal = 16.dp)) {
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
                if (state.hasSelectedItems) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                paymentSheetState.show()
                            }
                        }
                    ) {
                        Icon(Icons.Filled.EuroSymbol, contentDescription = "Pay")
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {
            BillList(table = table, billItems = state.billItems, addAction = vm::addItem)
        }
    }
}
