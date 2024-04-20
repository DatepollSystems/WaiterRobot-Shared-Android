package org.datepollsystems.waiterrobot.android.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.datepollsystems.waiterrobot.android.R
import org.datepollsystems.waiterrobot.android.ui.common.CustomDialog
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.LoginViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.cancel
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.inputLabel
import org.datepollsystems.waiterrobot.shared.generated.localization.inputPlaceholder
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.generated.localization.withQrCode
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(),
    navigator: NavController,
) {
    // val state by vm.collectAsState()

    vm.handleSideEffects(navigator)
    var showLinkInput by remember { mutableStateOf(false) }

    if (showLinkInput) {
        DebugLoginDialog(
            onDismiss = { showLinkInput = false },
            onLoginClick = {
                vm.onDebugLogin(it)
                showLinkInput = false
            }
        )
    }

    Scaffold(snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_round),
                contentDescription = "WaiterRobot icon",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
                    .padding(bottom = 20.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { showLinkInput = true }
                        )
                    }
            )
            Text(text = L.login.title(), style = MaterialTheme.typography.headlineMedium)
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                text = L.login.desc()
            )
            OutlinedButton(
                onClick = vm::openScanner
            ) {
                Text(text = L.login.withQrCode())
            }
        }
    }
}

@Composable
private fun DebugLoginDialog(
    onDismiss: () -> Unit,
    onLoginClick: (link: String) -> Unit
) {
    var link by remember { mutableStateOf("") }

    CustomDialog(
        onDismiss = onDismiss,
        title = "Login",
        actions = {
            TextButton(onClick = onDismiss) {
                Text(text = L.dialog.cancel())
            }
            Button(onClick = { onLoginClick(link) }) {
                Text(text = L.login.title())
            }
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = L.order.addNoteDialog.inputLabel()) },
            placeholder = { Text(text = L.order.addNoteDialog.inputPlaceholder()) },
            value = link,
            onValueChange = { link = it },
        )
    }
}
