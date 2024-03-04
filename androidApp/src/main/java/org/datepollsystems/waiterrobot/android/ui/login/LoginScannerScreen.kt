package org.datepollsystems.waiterrobot.android.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.android.ui.scanner.QrCodeScanner
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner.LoginScannerViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.cancel
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
fun LoginScannerScreen(
    vm: LoginScannerViewModel = koinViewModel(),
    navigator: NavController
) {
    val state by vm.collectAsState()
    vm.handleSideEffects(navigator)

    View(state) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QrCodeScanner { code ->
                val url = code.url?.url
                    ?: code.displayValue
                    ?: return@QrCodeScanner

                vm.onCode(url)
            }

            Text(
                modifier = Modifier.padding(vertical = 20.dp),
                textAlign = TextAlign.Center,
                text = L.login.scanner.desc()
            )

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = vm::goBack
            ) {
                Text(text = L.dialog.cancel())
            }
        }
    }
}
