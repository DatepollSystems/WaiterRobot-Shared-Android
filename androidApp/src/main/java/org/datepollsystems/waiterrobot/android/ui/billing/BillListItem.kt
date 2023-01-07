package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.datepollsystems.waiterrobot.android.ui.common.SwipeableListItem
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem

@Composable
fun BillListItem(
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
