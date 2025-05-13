package org.datepollsystems.waiterrobot.android.ui.billing

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.android.ui.core.ConfirmDialog
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.toast
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.android.ui.core.view.ViewStateOverlay
import org.datepollsystems.waiterrobot.shared.features.billing.presentation.BillingEffect
import org.datepollsystems.waiterrobot.shared.features.billing.presentation.BillingViewModel
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.Table
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
fun BillingScreen(
    table: Table,
    navigator: NavController,
    vm: BillingViewModel = koinViewModel { parametersOf(table) }
) {
    val state by vm.collectAsState()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequest = remember { FocusRequester() }
    var showConfirmGoBack by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }
    val paymentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    vm.handleSideEffects(navigator) {
        when (it) {
            is BillingEffect.Toast -> context.toast(it.message, Toast.LENGTH_SHORT)
            BillingEffect.ShowPaymentSheet -> showPaymentSheet = true
        }
    }

    val goBack: () -> Unit by rememberUpdatedState {
        when {
            state.hasCustomSelection -> showConfirmGoBack = true
            else -> vm.abortBill()
        }
    }

    BackHandler(onBack = goBack)

    if (showConfirmGoBack) {
        ConfirmDialog(
            title = MR.strings.billing_notSent_title.desc(),
            text = MR.strings.billing_notSent_desc.desc(),
            confirmText = MR.strings.dialog_closeAnyway.desc(),
            onConfirm = vm::abortBill,
            cancelText = MR.strings.billing_keepBill.desc(),
            onCancel = { showConfirmGoBack = false },
        )
    }

    ScaffoldView(
        title = MR.strings.billing_title(table.groupName, table.number),
        navigationIcon = {
            IconButton(onClick = goBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = vm::selectAll) {
                        Icon(Icons.Filled.DoneAll, contentDescription = "Select all items")
                    }
                    IconButton(onClick = vm::unselectAll) {
                        Icon(Icons.Filled.RemoveDone, contentDescription = "Unselect all items")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = MR.strings.billing_total() + ":",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = state.priceSum.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                floatingActionButton = {
                    if (state.hasSelectedItems) {
                        FloatingActionButton(
                            modifier = Modifier.padding(start = 16.dp),
                            onClick = vm::paySelection
                        ) {
                            Icon(Icons.Filled.EuroSymbol, contentDescription = "Pay")
                        }
                    }
                }
            )
        },
        bottomSheet = {
            if (showPaymentSheet) {
                LaunchedEffect(paymentSheetState.isVisible) {
                    if (paymentSheetState.isVisible) focusRequest.requestFocus()
                }

                ModalBottomSheet(
                    onDismissRequest = {
                        focusManager.clearFocus()
                        showPaymentSheet = false
                    },
                    sheetState = paymentSheetState,
                    dragHandle = null
                ) {
                    PaymentView(
                        sum = state.priceSum.toString(),
                        moneyGivenText = state.moneyGivenText,
                        moneyGiven = vm::moneyGiven,
                        moneyGivenInputFocusRequester = focusRequest,
                        change = state.change,
                        breakDownChange = vm::breakDownChange,
                        resetChangeBreakUp = vm::resetChange,
                        contactLessState = state.contactLessState,
                        onContactless = {
                            vm.initiateContactLessPayment()
                            focusManager.clearFocus()
                            coroutineScope.launch {
                                paymentSheetState.hide()
                            }.invokeOnCompletion {
                                if (!paymentSheetState.isVisible) {
                                    showPaymentSheet = false
                                }
                            }
                        },
                        onPayClick = {
                            vm.paySelection(paymentSheetShown = true)
                            focusManager.clearFocus()
                            coroutineScope.launch {
                                paymentSheetState.hide()
                            }.invokeOnCompletion {
                                if (!paymentSheetState.isVisible) {
                                    showPaymentSheet = false
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        ViewStateOverlay(
            modifier = Modifier.padding(padding),
            state = state.paymentState,
        ) {
            BillList(
                table = table,
                billItemResource = state.billItems,
                addAction = vm::addItem,
                refresh = vm::refreshBill
            )
        }
    }
}
