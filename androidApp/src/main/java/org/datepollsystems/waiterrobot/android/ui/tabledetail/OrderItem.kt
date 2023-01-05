package org.datepollsystems.waiterrobot.android.ui.tabledetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderItem

@Composable
fun OrderItem(item: OrderItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(0.15f),
            textAlign = TextAlign.Right,
            text = "${item.amount} x"
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            modifier = Modifier.weight(0.7f),
            text = item.name
        )
    }
}
