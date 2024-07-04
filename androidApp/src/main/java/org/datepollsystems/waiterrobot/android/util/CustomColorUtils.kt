package org.datepollsystems.waiterrobot.android.util

import android.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import org.datepollsystems.waiterrobot.android.ui.core.theme.darkColorScheme
import org.datepollsystems.waiterrobot.android.ui.core.theme.isDarkTheme
import org.datepollsystems.waiterrobot.android.ui.core.theme.lightColorScheme
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun ComposeColor.getContentColor(): ComposeColor =
    MaterialTheme.colorScheme.contentColorFor(this).takeOrElse {
        bestContrastColor(color1 = lightColorScheme.onSurface, color2 = darkColorScheme.onSurface)
    }

@Composable
fun ComposeColor.bestContrastColor(color1: ComposeColor, color2: ComposeColor): ComposeColor {
    return remember(this, color1, color2) {
        val colorInt = this.toArgb()
        val contrast1 = ColorUtils.calculateContrast(color1.toArgb(), colorInt)
        val contrast2 = ColorUtils.calculateContrast(color2.toArgb(), colorInt)
        if (contrast1 > contrast2) color1 else color2
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
