package org.datepollsystems.waiterrobot.android.ui.order

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.datepollsystems.waiterrobot.android.ui.common.CustomDialog
import org.datepollsystems.waiterrobot.android.ui.core.Preview
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.cancel
import org.datepollsystems.waiterrobot.shared.generated.localization.clear
import org.datepollsystems.waiterrobot.shared.generated.localization.inputLabel
import org.datepollsystems.waiterrobot.shared.generated.localization.inputPlaceholder
import org.datepollsystems.waiterrobot.shared.generated.localization.save
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.utils.euro

@Composable
fun AddNoteDialog(item: OrderItem, onDismiss: () -> Unit, onSave: (note: String?) -> Unit) {
    var note by remember { mutableStateOf(item.note ?: "") }

    CustomDialog(
        onDismiss = onDismiss,
        title = L.order.addNoteDialog.title(item.product.name),
        actions = {
            TextButton(onClick = onDismiss) {
                Text(text = L.dialog.cancel())
            }
            TextButton(onClick = { onSave(null) }) {
                Text(text = L.dialog.clear())
            }
            Button(onClick = { onSave(note) }) {
                Text(text = L.dialog.save())
            }
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = L.order.addNoteDialog.inputLabel()) },
            placeholder = { Text(text = L.order.addNoteDialog.inputPlaceholder()) },
            value = note,
            onValueChange = { note = it.take(120) },
            minLines = 3,
            maxLines = 3
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Right,
            text = "${note.count()}/120",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
@PreviewLightDark
private fun AddNoteDialogPreview() = Preview {
    AddNoteDialog(
        item = OrderItem(
            org.datepollsystems.waiterrobot.shared.features.order.models.Product(
                1,
                "Beer",
                4.euro,
                false,
                emptyList(),
                1
            ),
            1,
            "Test some note"
        ),
        {},
        { _ -> }
    )
}
