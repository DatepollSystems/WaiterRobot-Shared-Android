package org.datepollsystems.waiterrobot.android.ui.core.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme

@Composable
fun Preview(block: @Composable () -> Unit) {
    WaiterRobotTheme {
        Surface {
            block()
        }
    }
}
