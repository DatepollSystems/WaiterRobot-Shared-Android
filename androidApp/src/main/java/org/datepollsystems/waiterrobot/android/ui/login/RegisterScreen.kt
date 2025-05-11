package org.datepollsystems.waiterrobot.android.ui.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register.RegisterViewModel
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
fun RegisterScreen(
    vm: RegisterViewModel = koinViewModel(),
    navigator: NavController,
    registerLink: DeepLink.Auth.RegisterLink
) {
    val state by vm.collectAsState()
    vm.handleSideEffects(navigator)

    val focusManager = LocalFocusManager.current
    var name by remember { mutableStateOf("") }

    View(state) {
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
                text = MR.strings.register_name_desc()
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text(text = MR.strings.register_name_title()) }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = vm::cancel
                ) {
                    Text(MR.strings.dialog_cancel())
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { vm.onRegister(name, registerLink) }
                ) {
                    Text(MR.strings.register_login())
                }
            }

            Row(
                modifier = Modifier.padding(top = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Filled.Info, contentDescription = "Register information")
                Text(
                    textAlign = TextAlign.Start,
                    text = MR.strings.register_alreadyRegisteredInfo()
                )
            }
        }
    }
}
