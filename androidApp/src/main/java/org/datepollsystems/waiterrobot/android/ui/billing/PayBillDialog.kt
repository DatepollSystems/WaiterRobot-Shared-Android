package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.change
import org.datepollsystems.waiterrobot.shared.generated.localization.pay
import org.datepollsystems.waiterrobot.shared.generated.localization.total

@Composable
// TODO own Screen?
fun PayBillDialog(
    priceSum: String,
    changeText: String,
    moneyGivenText: String,
    dismiss: () -> Unit,
    onMoneyGivenChange: (String) -> Unit,
    pay: () -> Unit
) {
    Dialog(onDismissRequest = dismiss) {
        Surface(modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = L.billing.total() + ":",
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = priceSum,
                        style = MaterialTheme.typography.h6
                    )
                }
                // TODO add input for tip, divide through n Persons?
                OutlinedTextField(
                    value = moneyGivenText,
                    onValueChange = onMoneyGivenChange,
                    isError = changeText == "NaN" || changeText.startsWith("-"),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
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
                        text = changeText,
                        style = MaterialTheme.typography.h6
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        pay()
                        dismiss()
                    }
                ) {
                    Text(text = L.billing.pay())
                }
            }
        }
    }
}
