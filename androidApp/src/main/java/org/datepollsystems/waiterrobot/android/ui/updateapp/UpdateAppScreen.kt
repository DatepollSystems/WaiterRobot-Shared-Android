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
import org.datepollsystems.waiterrobot.android.BuildConfig
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.message
import org.datepollsystems.waiterrobot.shared.generated.localization.openStore
import org.datepollsystems.waiterrobot.shared.generated.localization.title

@Composable
@Destination
fun UpdateAppScreen() {
    Scaffold(
        snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) },
        topBar = {
            TopAppBar(
                title = { Text(L.app.forceUpdate.title()) },
            )
        },
    ) { contentPadding ->
        Surface(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = L.app.forceUpdate.message(),
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
                    Text(L.app.forceUpdate.openStore("Play Store"))
                }
            }
        }
    }
}
