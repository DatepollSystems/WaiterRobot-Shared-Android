package org.datepollsystems.waiterrobot.android.ui.order

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import org.datepollsystems.waiterrobot.android.ui.core.preview.Preview
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.android.ui.core.view.ViewStateOverlay
import org.datepollsystems.waiterrobot.android.ui.product.ProductList
import org.datepollsystems.waiterrobot.android.ui.product.ProductSearchScreen
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.order.domain.model.OrderItem
import org.datepollsystems.waiterrobot.shared.features.order.viewmodel.OrderState
import org.datepollsystems.waiterrobot.shared.features.order.viewmodel.OrderViewModel
import org.datepollsystems.waiterrobot.shared.features.product.domain.model.GroupedProducts
import org.datepollsystems.waiterrobot.shared.features.product.domain.model.Product
import org.datepollsystems.waiterrobot.shared.features.product.presentation.list.ProductListState
import org.datepollsystems.waiterrobot.shared.features.product.presentation.list.ProductListViewModel
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.Table
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.cent
import org.datepollsystems.waiterrobot.shared.utils.euro
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
fun OrderScreen(
    table: Table,
    initialItemId: Long? = null,
    navigator: NavController,
    orderVm: OrderViewModel = koinViewModel { parametersOf(table, initialItemId) },
    productVm: ProductListViewModel = koinViewModel(),
) {
    val orderState by orderVm.collectAsState()
    val productState by productVm.collectAsState()
    orderVm.handleSideEffects(navigator)
    productVm.handleSideEffects(navigator)

    OrderScreen(
        table = table,
        orderState = orderState,
        productState = productState,
        addAction = orderVm::addItem,
        refresh = productVm::refreshProducts,
        abort = orderVm::abortOrder,
        addNote = orderVm::addItemNote,
        send = orderVm::sendOrder,
        clearFilter = { productVm.filterProducts("") }
    )
}

@Composable
private fun OrderScreen(
    table: Table,
    orderState: OrderState,
    productState: ProductListState,
    addAction: (id: Long, amount: Int) -> Unit,
    refresh: () -> Unit,
    abort: () -> Unit,
    addNote: (OrderItem, String?) -> Unit,
    send: () -> Unit,
    clearFilter: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var noteDialogItem: OrderItem? by remember { mutableStateOf(null) }
    var showConfirmGoBack: Boolean by remember { mutableStateOf(false) }

    var showProductSheet by remember { mutableStateOf(false) }
    val productSearchSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val goBack: () -> Unit by rememberUpdatedState {
        when {
            orderState.currentOrder.isNotEmpty() -> showConfirmGoBack = true
            else -> abort()
        }
    }

    BackHandler(onBack = goBack)

    if (showConfirmGoBack) {
        ConfirmDialog(
            title = MR.strings.order_notSent_title.desc(),
            text = MR.strings.billing_notSent_desc.desc(),
            confirmText = MR.strings.dialog_closeAnyway.desc(),
            onConfirm = abort,
            cancelText = MR.strings.order_keepOrder.desc(),
            onCancel = { showConfirmGoBack = false }
        )
    }

    noteDialogItem?.let { item ->
        AddNoteDialog(
            item = item,
            onDismiss = { noteDialogItem = null },
            onSave = {
                addNote(item, it)
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
        bottomBar = {
            // TODO @FS: Replace with FlexibleBottomAppBar when available (https://m3.material.io/components/toolbars/overview)?
            BottomAppBar(
                actions = {
                    IconButton(onClick = { showProductSheet = true }) {
                        Icon(imageVector = Icons.Filled.Search, "Search product")
                    }
                },
                floatingActionButton = {
                    if (orderState.currentOrder.isNotEmpty()) {
                        FloatingActionButton(onClick = send) {
                            Icon(Icons.Filled.Send, contentDescription = "Send Order")
                        }
                    }
                }
            )
        },
        bottomSheet = {
            if (showProductSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showProductSheet = false },
                    sheetState = productSearchSheetState,
                    dragHandle = null,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentWindowInsets = { WindowInsets.statusBars }
                ) {
                    ProductSearchScreen(
                        onSelect = {
                            addAction(it.id, 1)
                            clearFilter()
                            coroutineScope.launch { productSearchSheetState.hide() }
                                .invokeOnCompletion { showProductSheet = false }
                        },
                        close = {
                            clearFilter()
                            coroutineScope.launch { productSearchSheetState.hide() }
                                .invokeOnCompletion { showProductSheet = false }
                        },
                    )
                }
            }
        }
    ) { padding ->
        ViewStateOverlay(
            modifier = Modifier.padding(padding),
            state = orderState.orderingState
        ) {
            Column {
                Surface(Modifier.weight(0.4f, fill = true)) {
                    if (orderState.currentOrder.isEmpty()) {
                        CenteredText(
                            modifier = Modifier.weight(1f),
                            text = MR.strings.order_product_add_desc(),
                            scrollAble = false
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(orderState.currentOrder, key = { it.product.id }) { orderItem ->
                                OrderListItem(
                                    id = orderItem.product.id,
                                    name = orderItem.product.name,
                                    amount = orderItem.amount,
                                    note = orderItem.note,
                                    addAction = addAction,
                                    onLongClick = { noteDialogItem = orderItem }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(thickness = 5.dp)

                ProductList(
                    Modifier.weight(0.6f, fill = true),
                    state = productState,
                    onSelect = { addAction(it.id, 1) },
                    refresh = refresh,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OrderScreenPreview() = Preview {
    OrderScreen(
        Table(1L, 1, "Outside", false),
        OrderState(
            _currentOrder = mapOf(
                1L to OrderItem(
                    Product(1L, "Beer 0.5", 480.cent, false, null, emptyList(), 1),
                    3,
                    null
                ),
                2L to OrderItem(
                    Product(2L, "Beer 0.33", 350.cent, false, null, emptyList(), 1),
                    3,
                    "not too cold"
                )
            )
        ),
        ProductListState(
            productGroups = Resource.Success(
                (0..3).map { group ->
                    GroupedProducts(
                        group.toLong(),
                        "Group $group",
                        group,
                        null,
                        products = (0..4).map {
                            Product(
                                group * 100L + it,
                                "Product $it",
                                it.euro + (it.cent / 10 * 10),
                                false,
                                null,
                                emptyList(),
                                it
                            )
                        }
                    )
                }
            )
        ),
        addAction = { _, _ -> },
        refresh = {},
        abort = {},
        addNote = { _, _ -> },
        send = {},
        clearFilter = {}
    )
}
