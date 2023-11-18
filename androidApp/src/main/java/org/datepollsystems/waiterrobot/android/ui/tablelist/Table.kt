package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

@Composable
fun Table(table: Table, onClick: (Table) -> Unit) {
    val cardShape = RoundedCornerShape(10)
    Card(
        shape = cardShape,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(cardShape)
            .clickable { onClick(table) }
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = table.number.toString(),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
            if (table.hasOrders) {
                val size = 10.dp
                Box(
                    modifier = Modifier
                        .size(size)
                        .offset(x = -(maxWidth / 2 - size), y = -(maxHeight / 2 - size))
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.onPrimary)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TablePreview() {
    WaiterRobotTheme {
        Table(table = Table(0, 1, "Group 1", hasOrders = true), onClick = {})
    }
}
