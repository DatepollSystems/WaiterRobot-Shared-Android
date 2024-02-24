package org.datepollsystems.waiterrobot.android.ui.tablelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.core.Preview
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

@Composable
fun Table(table: Table, onClick: (Table) -> Unit) {
    val cardShape = RoundedCornerShape(10)
    Card(
        shape = cardShape,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(cardShape)
            .clickable { onClick(table) }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = table.number.toString(),
                style = MaterialTheme.typography.headlineMedium,
            )
            if (table.hasOrders) {
                val size = maxWidth * 0.1f
                Box(
                    modifier = Modifier
                        .size(size)
                        .offset(
                            x = -(maxWidth / 2 - size / 2),
                            y = -(maxHeight / 2 - size / 2)
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TablePreview() = Preview {
    Table(table = Table(0, 1, "Group 1", hasOrders = true), onClick = {})
}
