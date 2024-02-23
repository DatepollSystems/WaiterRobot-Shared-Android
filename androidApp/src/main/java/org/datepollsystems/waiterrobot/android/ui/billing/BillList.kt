package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.datepollsystems.waiterrobot.android.ui.common.CenteredText
import org.datepollsystems.waiterrobot.android.ui.common.SwipeableListItem
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.noOpenBill

@Composable
fun BillList(
    table: Table,
    billItems: List<BillItem>,
    addAction: (id: Long, amount: Int) -> Unit
) {
    Column {
        if (billItems.isEmpty()) {
            CenteredText(
                text = L.billing.noOpenBill(table.number.toString(), table.groupName),
                scrollAble = true
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(billItems, key = BillItem::productId) { billItem ->
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
    swipeAdd = { addAction(item.productId, 1) },
    swipeRemove = { addAction(item.productId, -1) },
    onClick = { addAction(item.productId, 1) }
) {
    Text(
        modifier = Modifier.weight(0.2f),
        textAlign = TextAlign.Right,
        text = "${item.ordered} x"
    )
    Spacer(modifier = Modifier.weight(0.03f))
    Text(
        modifier = Modifier.weight(0.6f),
        text = item.name
    )
    Spacer(modifier = Modifier.weight(0.03f))
    Text(
        modifier = Modifier.weight(0.15f),
        textAlign = TextAlign.Right,
        text = item.selectedForBill.toString()
    )
    Spacer(modifier = Modifier.weight(0.03f))
    Text(
        modifier = Modifier.weight(0.25f),
        textAlign = TextAlign.Right,
        text = item.priceSum.toString()
    )
}
