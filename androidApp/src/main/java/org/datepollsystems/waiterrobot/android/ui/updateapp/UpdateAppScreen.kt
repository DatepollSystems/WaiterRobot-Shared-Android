package org.datepollsystems.waiterrobot.android.ui.updateapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.core.LocalScaffoldState

@Composable
@Destination
fun UpdateAppScreen() {
    Scaffold(
        scaffoldState = LocalScaffoldState.current,
        topBar = {
            TopAppBar(
                title = { Text("Update App") },
            )
        },
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Text("Your installed app version is no longer supported. Please update now.")
        }
    }
}
