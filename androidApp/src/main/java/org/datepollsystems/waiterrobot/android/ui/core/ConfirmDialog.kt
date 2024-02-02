package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    confirmText: String,
    onConfirm: () -> Unit,
    cancelText: String,
    onCancel: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(cancelText)
            }
        },
        onDismissRequest = onCancel
    )
}
