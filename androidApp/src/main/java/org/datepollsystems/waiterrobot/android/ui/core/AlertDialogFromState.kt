package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.datepollsystems.waiterrobot.shared.core.viewmodel.DialogState

@Composable
fun AlertDialogFromState(dialog: DialogState?) {
    if (dialog == null) return
    AlertDialog(
        onDismissRequest = dialog.onDismiss,
        title = { Text(text = dialog.title) },
        text = { Text(text = dialog.text) },
        confirmButton = {
            Button(onClick = dialog.primaryButton.action) {
                Text(text = dialog.primaryButton.text)
            }
        },
        dismissButton = dialog.secondaryButton?.let { button ->
            {
                TextButton(onClick = button.action) {
                    Text(text = button.text)
                }
            }
        }
    )
}
