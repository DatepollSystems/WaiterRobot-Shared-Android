package org.datepollsystems.waiterrobot.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun WaiterRobotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors
    } else {
        lightColors
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}

private val lightColors = lightColors(
    // TODO colorScheme
)

private val darkColors = darkColors(
    // TODO colorScheme
)