package org.datepollsystems.waiterrobot.android.ui.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register.RegisterEffect
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register.RegisterViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.*
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
@Destination
fun RegisterScreen(
    vm: RegisterViewModel = getViewModel(),
    navigator: NavController,
    createToken: String
) {
    vm.collectSideEffect { handleSideEffect(it, navigator) }

    val focusManager = LocalFocusManager.current
    var name by remember { mutableStateOf("") }

    // TODO same as on iOS
    Column(
        modifier = Modifier
            .padding(20.dp)
            .padding(top = 40.dp)
            .pointerInput("hide_keyboard") {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = L.register.name.desc()
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text(text = L.register.name.title()) }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = vm::cancel
            ) {
                Text(L.dialog.cancel())
            }
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = { vm.onRegister(name, createToken) }
            ) {
                Text(L.register.login())
            }
        }

        Row(
            modifier = Modifier.padding(top = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Register information")
            Text(
                textAlign = TextAlign.Start,
                text = L.register.alreadyRegisteredInfo()
            )
        }
    }
}

private fun handleSideEffect(effect: RegisterEffect, navigator: NavController) {
    when (effect) {
        is RegisterEffect.Navigate -> navigator.handleNavAction(effect.action)
    }
}
