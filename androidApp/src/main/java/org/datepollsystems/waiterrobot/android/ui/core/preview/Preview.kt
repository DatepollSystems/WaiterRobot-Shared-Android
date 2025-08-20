package org.datepollsystems.waiterrobot.android.ui.core.preview

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme

@Composable
fun Preview(block: @Composable () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    WaiterRobotTheme {
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            Surface {
                block()
            }
        }
    }
}
