package org.datepollsystems.waiterrobot.android.ui.core.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState

@Composable
fun ErrorDialog(error: ViewState.Error) {
    AlertDialog(
        onDismissRequest = error.onDismiss,
        confirmButton = {
            Button(onClick = error.onDismiss) {
                Text("OK")
            }
        },
        title = {
            Text(text = error.title)
        },
        text = {
            Text(text = error.message)
        }
    )
}
