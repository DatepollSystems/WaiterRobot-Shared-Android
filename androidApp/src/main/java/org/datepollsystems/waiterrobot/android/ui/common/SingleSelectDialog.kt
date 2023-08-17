package org.datepollsystems.waiterrobot.android.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Surface(shape = RoundedCornerShape(10.dp)) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = title)

                Spacer(modifier = Modifier.height(10.dp))

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
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick(item) }
        )
        Text(
            text = itemText(item),
            style = MaterialTheme.typography.body1.merge(),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
