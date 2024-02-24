package org.datepollsystems.waiterrobot.android.ui.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.common.SwipeableListItem
import org.datepollsystems.waiterrobot.android.ui.core.Preview

@Composable
fun OrderListItem(
    id: Long,
    name: String,
    amount: Int,
    note: String?,
    addAction: (id: Long, amount: Int) -> Unit,
    onLongClick: () -> Unit
) = SwipeableListItem(
    swipeAdd = { addAction(id, 1) },
    swipeRemove = { addAction(id, -1) },
    onClick = { addAction(id, 1) },
    onLongClick = onLongClick
) {
    Row {
        Text(
            modifier = Modifier.weight(0.2f),
            textAlign = TextAlign.Right,
            text = "${amount}x"
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(0.8f)
        ) {
            Text(
                text = name
            )
            if (note != null) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
@PreviewFontScale
private fun OrderListItemPreview() = Preview {
    Column {
        OrderListItem(
            id = 1,
            name = "Beer",
            amount = 100,
            note = "test Note",
            addAction = { _, _ -> },
            onLongClick = {}
        )
        OrderListItem(
            id = 1,
            name = "Beer",
            amount = 10,
            note = null,
            addAction = { _, _ -> },
            onLongClick = {}
        )
    }
}
