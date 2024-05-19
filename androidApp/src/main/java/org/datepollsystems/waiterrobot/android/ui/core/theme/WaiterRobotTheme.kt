package org.datepollsystems.waiterrobot.android.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WaiterRobotTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
    ) {
        MaterialTheme(
            colorScheme = if (useDarkTheme) darkColorScheme else lightColorScheme,
            content = content
        )
    }
}

private val lightColorScheme = lightColorScheme(
    // TODO colorScheme
)

private val darkColorScheme = darkColorScheme(
    // TODO colorScheme
)
