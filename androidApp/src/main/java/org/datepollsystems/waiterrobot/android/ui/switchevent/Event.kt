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
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.datepollsystems.waiterrobot.android.ui.core.Preview
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.hours

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
            event.startDate?.let {
                Text(
                    // TODO This needs to be fixed
                    text = it.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
                        .format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

@Preview
@Composable
fun EventPreview() = Preview {
    Event(
        event = Event(
            id = 1,
            name = "Test Event",
            startDate = Clock.System.now().minus(1.hours),
            endDate = Clock.System.now().plus(1.hours),
            city = "Vienna",
            organisationId = 1,
            stripeSettings = Event.StripeSettings.Disabled,
        )
    )
}
