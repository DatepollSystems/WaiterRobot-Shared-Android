package org.datepollsystems.waiterrobot.android.ui.billing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.preview.Preview
import org.datepollsystems.waiterrobot.shared.features.billing.presentation.BillingState
import org.datepollsystems.waiterrobot.shared.features.billing.presentation.ChangeBreakUp
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.Money
import org.datepollsystems.waiterrobot.shared.utils.euro

@Composable
fun PaymentView(
    sum: String,
    moneyGivenText: String,
    moneyGiven: (String) -> Unit,
    moneyGivenInputFocusRequester: FocusRequester,
    change: BillingState.Change?,
    breakDownChange: (ChangeBreakUp) -> Unit,
    resetChangeBreakUp: () -> Unit,
    contactLessState: BillingState.ContactLessState,
    onPayClick: () -> Unit,
    onContactless: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = sum,
            style = MaterialTheme.typography.headlineMedium
        )

        if (contactLessState != BillingState.ContactLessState.DISABLED) {
            Row {
                Button(
                    onClick = onContactless,
                    enabled = moneyGivenText.isEmpty() && contactLessState == BillingState.ContactLessState.ENABLED
                ) {
                    Icon(
                        Icons.Filled.Contactless,
                        contentDescription = MR.strings.billing_pay_card()
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(MR.strings.billing_pay_card())
                }
            }
        }

        // TODO add input for tip, divide through n Persons?
        OutlinedTextField(
            label = { Text(text = MR.strings.billing_given()) },
            placeholder = { Text(text = "0.00") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(moneyGivenInputFocusRequester),
            value = moneyGivenText,
            onValueChange = moneyGiven,
            isError = change == null || change.amount.isNegative,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            trailingIcon = {
                IconButton(onClick = { moneyGiven("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "clear")
                }
            }
        )

        Change(change = change, breakDownChange, resetChangeBreakUp)

        ExtendedFloatingActionButton(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = onPayClick,
            text = { Text(MR.strings.billing_pay_cash()) },
            icon = {}
        )
    }
}

@Composable
fun Change(
    change: BillingState.Change?,
    breakDownChange: (ChangeBreakUp) -> Unit,
    resetChangeBreakUp: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = MR.strings.billing_change() + ":",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = change?.amount?.toString() ?: "??? €",
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (change != null && !change.amount.isNegative && change.breakUp.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                change.breakUp.forEach {
                    ChangeChip(
                        quantity = it.quantity,
                        amount = it.amount,
                        onClick = { breakDownChange(it) }
                    )
                }
                if (change.brokenDown) {
                    AssistChip(
                        onClick = resetChangeBreakUp,
                        label = { Text(text = "Reset") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Restore,
                                contentDescription = "Reset"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChangeChip(quantity: Int, amount: Money, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(text = "${quantity}x ${amount.toFullString()}") },
    )
}

@PreviewFontScale
@PreviewLightDark
@Composable
private fun PaymentPreview() = Preview {
    PaymentView(
        sum = "39.30",
        moneyGivenText = "50",
        moneyGiven = { },
        moneyGivenInputFocusRequester = FocusRequester(),
        change = BillingState.Change("19.70".euro, brokenDown = true),
        breakDownChange = {},
        resetChangeBreakUp = {},
        contactLessState = BillingState.ContactLessState.ENABLED,
        onPayClick = {},
        onContactless = {},
    )
}
