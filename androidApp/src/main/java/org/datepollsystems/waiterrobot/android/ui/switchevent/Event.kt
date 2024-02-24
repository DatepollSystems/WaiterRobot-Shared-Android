package org.datepollsystems.waiterrobot.android.ui.switchevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event

@Composable
fun Event(event: Event) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = event.name
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = event.city,
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
            event.date?.let {
                Text(
                    text = event.date.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}
