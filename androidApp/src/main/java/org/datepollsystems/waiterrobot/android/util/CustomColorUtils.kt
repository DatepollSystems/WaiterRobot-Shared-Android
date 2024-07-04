package org.datepollsystems.waiterrobot.android.util

import android.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import org.datepollsystems.waiterrobot.android.ui.core.theme.darkColorScheme
import org.datepollsystems.waiterrobot.android.ui.core.theme.isDarkTheme
import org.datepollsystems.waiterrobot.android.ui.core.theme.lightColorScheme
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun ComposeColor.getContentColor(): ComposeColor {
    return remember(this) {
        val colorInt = this.toArgb()
        val lightContrast =
            ColorUtils.calculateContrast(lightColorScheme.onSurface.toArgb(), colorInt)
        val darkContrast =
            ColorUtils.calculateContrast(darkColorScheme.onSurface.toArgb(), colorInt)
        if (lightContrast > darkContrast) lightColorScheme.onSurface else darkColorScheme.onSurface
    }
}

@Composable
fun ComposeColor.desaturateOnDarkMode(): ComposeColor {
    val darkTheme = MaterialTheme.colorScheme.isDarkTheme
    return remember(this, darkTheme) {
        if (darkTheme) {
            val colorInt = this.toArgb()
            val result = FloatArray(size = 3)
            Color.colorToHSV(colorInt, result)
            result[1] *= 0.6f // desaturate the color
            ComposeColor.hsv(result[0], result[1], result[2])
        } else {
            this
        }
    }
}
