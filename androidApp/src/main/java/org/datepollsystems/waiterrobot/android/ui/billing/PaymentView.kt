package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.common.FloatingActionButton
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.change

@Composable
fun PaymentView(
    sum: String,
    moneyGivenText: String,
    moneyGiven: (String) -> Unit,
    moneyGivenInputFocusRequester: FocusRequester,
    change: String,
    onPayClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = sum,
            style = MaterialTheme.typography.h4
        )

        // TODO only show when there are other payment options available
        Row {
            // TODO one button for each available payment option (except cash)
            Button(
                onClick = { /*TODO*/ },
                enabled = moneyGivenText.isEmpty()
            ) {
                Icon(Icons.Filled.Contactless, contentDescription = "Contactless")
            }
        }

        // TODO add input for tip, divide through n Persons?
        OutlinedTextField(
            label = { Text(text = "Gegeben") },
            placeholder = { Text(text = "0.00") },
            modifier = Modifier.focusRequester(moneyGivenInputFocusRequester),
            value = moneyGivenText,
            onValueChange = moneyGiven,
            isError = change == "NaN" || change.startsWith("-"),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            trailingIcon = {
                IconButton(onClick = { moneyGiven("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "clear")
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = L.billing.change() + ":",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = change,
                style = MaterialTheme.typography.h6
            )
        }

        FloatingActionButton(
            onClick = onPayClick
        ) {
            Icon(Icons.Filled.AttachMoney, contentDescription = "Cash")
        }
    }
}
