package org.datepollsystems.waiterrobot.android.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.cancel
import org.datepollsystems.waiterrobot.shared.generated.localization.clear
import org.datepollsystems.waiterrobot.shared.generated.localization.inputLabel
import org.datepollsystems.waiterrobot.shared.generated.localization.inputPlaceholder
import org.datepollsystems.waiterrobot.shared.generated.localization.save
import org.datepollsystems.waiterrobot.shared.generated.localization.title

@Composable
fun AddNoteDialog(item: OrderItem, onDismiss: () -> Unit, onSave: (note: String?) -> Unit) {
    var note by remember { mutableStateOf(item.note ?: "") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                ProvideTextStyle(value = MaterialTheme.typography.subtitle1) {
                    Text(text = L.order.addNoteDialog.title(item.product.name))
                }
                Spacer(modifier = Modifier.height(10.dp))
                ProvideTextStyle(value = MaterialTheme.typography.body2) {
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
                        fontSize = MaterialTheme.typography.caption.fontSize
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
            }
        }
    }
}
