package org.datepollsystems.waiterrobot.android.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.datepollsystems.waiterrobot.shared.features.order.models.OrderItem
import org.datepollsystems.waiterrobot.shared.generated.localization.*

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
                        onValueChange = { note = it.take(120) }
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
