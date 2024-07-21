package org.datepollsystems.waiterrobot.android.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
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
fun ComposeColor.desaturateOnDarkMode(force: Boolean = false): ComposeColor {
    val darkTheme = MaterialTheme.colorScheme.isDarkTheme || force
    return remember(this, darkTheme) {
        if (darkTheme) {
            val colorInt = this.toArgb()
            val result = FloatArray(size = 3)
            ColorUtils.colorToHSL(colorInt, result)
            result[1] *= 0.6f // desaturate the color
            ComposeColor.hsl(result[0], result[1], result[2])
        } else {
            this
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
@Preview
fun Test() {
    val colors = listOf(
        "#ebefff", "#1b2347", "#c9d1fb", "#6750A4", "#D4Dbfa", "#607dff", "#f8ffa8", "#efff32",
        "#e0ffc0", "#acff56", "#ffdbf7", "#ff60Dc",
    )
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(lightColorScheme.surface)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "Original",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            colors.forEach {
                val color = ComposeColor(it.toColorInt())
                Box(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .background(color)
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = color.toArgb().toHexString().substring(2),
                        color = color.getContentColor()
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .background(darkColorScheme.surface)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "Desaturated",
                fontSize = 20.sp,
                color = darkColorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            colors.forEach {
                val color = ComposeColor(it.toColorInt()).desaturateOnDarkMode(true)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .background(color)
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = color.toArgb().toHexString().substring(2),
                        color = color.getContentColor()
                    )
                }
            }
        }
    }
}
