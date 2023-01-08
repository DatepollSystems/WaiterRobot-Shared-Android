package org.datepollsystems.waiterrobot.android.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun WaiterRobotTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (useDarkTheme) darkColors else lightColors,
        content = content
    )
}

private val lightColors = lightColors(
    // TODO colorScheme
)

private val darkColors = darkColors(
    // TODO colorScheme
)
