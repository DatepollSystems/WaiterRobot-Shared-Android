package org.datepollsystems.waiterrobot.android.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.android.ui.scanner.QrCodeScanner
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner.LoginScannerEffect
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner.LoginScannerViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.cancel
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
@Destination
fun LoginScannerScreen(
    vm: LoginScannerViewModel = getViewModel(),
    navigator: NavController
) {
    vm.collectSideEffect { handleSideEffect(it, navigator) }

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

private fun handleSideEffect(effect: LoginScannerEffect, navigator: NavController) {
    when (effect) {
        is LoginScannerEffect.Navigate -> navigator.handleNavAction(effect.action)
    }
}
