package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.retry
import org.datepollsystems.waiterrobot.shared.utils.getLocalizedUserMessage

@Composable
fun ErrorBar(
    modifier: Modifier = Modifier,
    message: String,
    retryAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.error)
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = if (retryAction == null) 16.dp else 8.dp,
                bottom = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = message,
            color = MaterialTheme.colors.onError,
            textAlign = TextAlign.Start
        )
        if (retryAction != null) {
            TextButton(
                modifier = Modifier
                    .padding(start = 16.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onError),
                onClick = retryAction
            ) {
                Text(text = L.exceptions.retry(), fontWeight = FontWeight.Bold, maxLines = 2)
            }
        }
    }
}

@Composable
fun ErrorBar(
    modifier: Modifier = Modifier,
    exception: Throwable,
    retryAction: (() -> Unit)? = null
) = ErrorBar(modifier, exception.getLocalizedUserMessage(), retryAction)

@Preview
@Composable
private fun ErrorBarPreview() = Preview {
    ErrorBar(message = "Some exception message. Please try again.", retryAction = {})
}
