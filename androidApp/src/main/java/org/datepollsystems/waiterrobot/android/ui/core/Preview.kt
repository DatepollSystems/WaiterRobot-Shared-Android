package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme
import org.datepollsystems.waiterrobot.shared.generated.localization.localizationContext

@Composable
fun Preview(block: @Composable () -> Unit) {
    localizationContext = LocalContext.current
    WaiterRobotTheme {
        Surface {
            block()
        }
    }
}
