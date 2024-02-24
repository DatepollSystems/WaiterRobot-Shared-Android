package org.datepollsystems.waiterrobot.android.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.datepollsystems.waiterrobot.android.ui.core.Preview
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.title

@Composable
fun <T : Any> SingleSelectDialog(
    title: String,
    options: List<T>,
    optionId: (T) -> Any,
    optionText: @Composable (T) -> String,
    selected: T,
    onSelect: (T) -> Unit,
    onDismissRequest: () -> Unit
) {
    CustomDialog(
        onDismiss = onDismissRequest,
        title = title,
        actions = null
    ) {
        LazyColumn {
            items(items = options, key = { optionId(it) }) { option ->
                RadioButtonRow(
                    item = option,
                    selected = selected == option,
                    itemText = optionText,
                    onClick = {
                        onSelect(it)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
private fun <T : Any> RadioButtonRow(
    item: T,
    itemText: @Composable (T) -> String,
    selected: Boolean,
    onClick: (T) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = { onClick(item) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick(item) }
        )
        Text(
            text = itemText(item),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
@PreviewLightDark
private fun SingleSelectDialogPreview() = Preview {
    Column {
        SingleSelectDialog(
            title = L.settings.darkMode.title(),
            options = AppTheme.entries,
            optionId = AppTheme::ordinal,
            optionText = { it.settingsText() },
            selected = AppTheme.SYSTEM,
            onSelect = { },
            onDismissRequest = { }
        )
    }
}
