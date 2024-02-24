package org.datepollsystems.waiterrobot.android.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.datepollsystems.waiterrobot.android.R
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.LoginViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
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
            )
            Text(text = L.login.title(), style = MaterialTheme.typography.h4)
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                text = L.login.desc()
            )
            OutlinedButton(onClick = vm::openScanner) {
                Text(text = L.login.withQrCode())
            }
        }
    }
}
