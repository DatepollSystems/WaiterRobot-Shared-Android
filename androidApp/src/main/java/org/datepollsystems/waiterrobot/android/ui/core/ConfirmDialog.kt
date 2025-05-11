package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.desc.StringDesc

@Composable
fun ConfirmDialog(
    title: StringDesc,
    text: StringDesc,
    confirmText: StringDesc,
    onConfirm: () -> Unit,
    cancelText: StringDesc,
    onCancel: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = title())
        },
        text = {
            Text(text = text())
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText())
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(cancelText())
            }
        },
        onDismissRequest = onCancel
    )
}
