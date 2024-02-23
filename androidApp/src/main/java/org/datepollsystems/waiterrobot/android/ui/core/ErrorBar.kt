package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
    var expanded by remember { mutableStateOf(false) }
    val maxLines = remember(expanded) { if (expanded) Int.MAX_VALUE else 2 }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.error)
            .clickable { expanded = !expanded }
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = if (retryAction == null) 16.dp else 8.dp,
                bottom = 8.dp
            )
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(0.65f),
            text = message,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onError,
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.width(16.dp))
        if (retryAction != null) {
            TextButton(
                modifier = Modifier.weight(0.35f),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onError),
                onClick = retryAction
            ) {
                Text(
                    text = L.exceptions.retry(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    maxLines = maxLines
                )
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
@PreviewLightDark
@PreviewFontScale
@Composable
private fun ErrorBarPreview() = Preview {
    ErrorBar(message = "Some exception message. Please try again.", retryAction = {})
}
