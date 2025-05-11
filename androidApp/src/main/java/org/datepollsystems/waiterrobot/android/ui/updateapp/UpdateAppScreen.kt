package org.datepollsystems.waiterrobot.android.ui.updateapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.datepollsystems.waiterrobot.android.BuildConfig
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.shared.localization.MR

@Composable
@Destination<RootGraph>
fun UpdateAppScreen() {
    Scaffold(
        snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) },
        topBar = {
            TopAppBar(
                title = { Text(MR.strings.app_forceUpdate_title()) },
            )
        },
    ) { contentPadding ->
        Surface(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = MR.strings.app_forceUpdate_message(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(15.dp))

                val uriHandler = LocalUriHandler.current
                Button(
                    onClick = {
                        runCatching {
                            uriHandler.openUri("market://details?id=${BuildConfig.APPLICATION_ID}")
                        }.onFailure {
                            uriHandler.openUri(
                                "https://play.google.com/store/apps/details?id=" +
                                    BuildConfig.APPLICATION_ID
                            )
                        }
                    }
                ) {
                    Text(MR.strings.app_forceUpdate_openStore("Play Store"))
                }
            }
        }
    }
}
