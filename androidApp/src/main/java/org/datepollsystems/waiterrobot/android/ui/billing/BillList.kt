package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.common.SwipeableListItem
import org.datepollsystems.waiterrobot.android.ui.core.ErrorBar
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.preview.Preview
import org.datepollsystems.waiterrobot.android.ui.core.view.LoadingView
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.billing.domain.model.BillItem
import org.datepollsystems.waiterrobot.shared.features.table.domain.model.Table
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.euro

@Composable
fun ColumnScope.BillList(
    table: Table,
    billItemResource: Resource<List<BillItem>>,
    addAction: (id: Long, amount: Int) -> Unit,
    refresh: () -> Unit,
) {
    if (billItemResource is Resource.Loading && billItemResource.data == null) {
        LoadingView()
    } else {
        val billItems = billItemResource.data

        if (billItemResource is Resource.Error) {
            ErrorBar(message = billItemResource.userMessage, retryAction = refresh)
        }

        if (billItems.isNullOrEmpty()) {
            CenteredText(
                text = MR.strings.billing_noOrder(table.groupName, table.number.toString()),
                scrollAble = true
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(billItems, key = BillItem::baseProductId) { billItem ->
                    BillListItem(
                        item = billItem,
                        addAction = addAction
                    )
                }
            }
        }
    }
}

@Composable
private fun BillListItem(
    item: BillItem,
    modifier: Modifier = Modifier,
    addAction: (id: Long, amount: Int) -> Unit
) = SwipeableListItem(
    modifier = modifier,
    swipeAdd = { addAction(item.baseProductId, 1) },
    swipeRemove = { addAction(item.baseProductId, -1) },
    onClick = { addAction(item.baseProductId, 1) }
) {
    Text(
        modifier = Modifier.weight(0.2f),
        textAlign = TextAlign.Right,
        text = "${item.ordered}x"
    )
    Spacer(modifier = Modifier.width(16.dp))
    Text(
        modifier = Modifier.weight(0.6f),
        text = item.name
    )
    Spacer(modifier = Modifier.width(16.dp))
    Text(
        modifier = Modifier.weight(0.15f),
        textAlign = TextAlign.Right,
        text = item.selectedForBill.toString()
    )
    Spacer(modifier = Modifier.width(16.dp))
    Text(
        modifier = Modifier.weight(0.25f),
        textAlign = TextAlign.Right,
        text = item.priceSum.toString()
    )
}

@PreviewLightDark
@Composable
private fun BillListPreview() = Preview {
    Column {
        BillList(
            table = Table(1, 1, "Outside", false),
            billItemResource = Resource.Success(
                listOf(
                    BillItem(1, "Beer", 10, 5, 4.euro),
                    BillItem(2, "Fries", 5, 0, 3.euro),
                )
            ),
            addAction = { _, _ -> },
            refresh = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun BillListItemPreview() = Preview {
    BillListItem(
        item = BillItem(1, "Beer", 10, 5, 4.euro),
        addAction = { _, _ -> }
    )
}
