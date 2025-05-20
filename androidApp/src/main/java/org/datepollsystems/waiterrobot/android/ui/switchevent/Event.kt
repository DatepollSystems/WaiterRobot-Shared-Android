package org.datepollsystems.waiterrobot.android.ui.switchevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.preview.BooleanPreviewProvider
import org.datepollsystems.waiterrobot.android.ui.core.preview.Preview
import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.model.Event
import org.datepollsystems.waiterrobot.shared.localization.MR
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.hours

@Composable
fun Event(event: Event) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = event.name)

            if (event.isDemo) {
                Badge {
                    Text(MR.strings.switchEvent_demoEvent())
                }
            }
        }

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
fun EventPreview(@PreviewParameter(BooleanPreviewProvider::class) isDemo: Boolean) = Preview {
    Event(
        event = Event(
            id = 1,
            name = "Test Event",
            startDate = Clock.System.now().minus(1.hours),
            endDate = Clock.System.now().plus(1.hours),
            city = "Vienna",
            organisationId = 1,
            stripeSettings = Event.StripeSettings.Disabled,
            isDemo = isDemo
        )
    )
}
